#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct code {
    unsigned int freq;
    char *binary_code;
} Code;

typedef struct node {
    char character;
    unsigned freq;
    struct node *left, *right;
} Node;

typedef struct min_heap {
    unsigned size;
    unsigned capacity;
    Node **array;
} MinHeap;

Node* create_node(char character, unsigned freq) {
    Node* temp = (Node*)malloc(sizeof(Node));
    temp->left = temp->right = NULL;
    temp->character = character;
    temp->freq = freq;
    return temp;
}

MinHeap* create_min_heap(unsigned capacity) {
    MinHeap* minHeap = (MinHeap*)malloc(sizeof(MinHeap));
    minHeap->size = 0;
    minHeap->capacity = capacity;
    minHeap->array = (Node**)malloc(minHeap->capacity * sizeof(Node*));
    return minHeap;
}

void swap_nodes(Node** a, Node** b) {
    Node* t = *a;
    *a = *b;
    *b = t;
}

void min_heapify(MinHeap* minHeap, int idx) {
    int smallest = idx;
    int left = 2 * idx + 1;
    int right = 2 * idx + 2;

    if (left < minHeap->size && minHeap->array[left]->freq < minHeap->array[smallest]->freq)
        smallest = left;

    if (right < minHeap->size && minHeap->array[right]->freq < minHeap->array[smallest]->freq)
        smallest = right;

    if (smallest != idx) {
        swap_nodes(&minHeap->array[smallest], &minHeap->array[idx]);
        min_heapify(minHeap, smallest);
    }
}

int is_size_one(MinHeap* minHeap) {
    return (minHeap->size == 1);
}

Node* extract_min(MinHeap* minHeap) {
    Node* temp = minHeap->array[0];
    minHeap->array[0] = minHeap->array[minHeap->size - 1];
    --minHeap->size;
    min_heapify(minHeap, 0);
    return temp;
}

void insert_min_heap(MinHeap* minHeap, Node* node) {
    ++minHeap->size;
    int i = minHeap->size - 1;
    while (i && (node->freq < minHeap->array[(i - 1) / 2]->freq ||
                 (node->freq == minHeap->array[(i - 1) / 2]->freq && node->character < minHeap->array[(i - 1) / 2]->character))) {
        minHeap->array[i] = minHeap->array[(i - 1) / 2];
        i = (i - 1) / 2;
    }
    minHeap->array[i] = node;
}

void build_min_heap(MinHeap* minHeap) {
    int n = minHeap->size - 1;
    for (int i = (n - 1) / 2; i >= 0; --i)
        min_heapify(minHeap, i);
}

MinHeap* create_and_build_min_heap(char data[], unsigned freq[], int size) {
    MinHeap* minHeap = create_min_heap(size);
    for (int i = 0; i < size; ++i)
        minHeap->array[i] = create_node(data[i], freq[i]);
    minHeap->size = size;
    build_min_heap(minHeap);
    return minHeap;
}

Node* build_huffman_tree(char data[], unsigned freq[], int size) {
    Node *left, *right, *top;
    MinHeap* minHeap = create_and_build_min_heap(data, freq, size);

    while (!is_size_one(minHeap)) {
        left = extract_min(minHeap);
        right = extract_min(minHeap);
        top = create_node('$', left->freq + right->freq);
        top->left = left;
        top->right = right;
        insert_min_heap(minHeap, top);
    }

    return extract_min(minHeap);
}

void generate_codes(Node* root, char *code, int top, Code *codeTable) {
    if (root->left) {
        code[top] = '0';
        generate_codes(root->left, code, top + 1, codeTable);
    }

    if (root->right) {
        code[top] = '1';
        generate_codes(root->right, code, top + 1, codeTable);
    }

    if (!(root->left) && !(root->right)) {
        code[top] = '\0';
        codeTable[(unsigned char)root->character].binary_code = (char*)malloc((top + 1) * sizeof(char));
        if (codeTable[(unsigned char)root->character].binary_code == NULL) {
            fprintf(stderr, "Memory allocation failed for binary_code\n");
            exit(1);
        }
        strcpy(codeTable[(unsigned char)root->character].binary_code, code);
    }
}

void encode_text(FILE *inputFile, FILE *encodedFile, Code *codeTable) {
    int c;
    while ((c = fgetc(inputFile)) != EOF && c != '\n') {
        if (codeTable[c].binary_code != NULL) {
            fputs(codeTable[c].binary_code, encodedFile);
        } else {
            fprintf(stderr, "No binary code found for character: %c\n", c);
            exit(1);
        }
    }
}

void sort_code_table(char data[], unsigned freq[], char *binary_code[], int size) {
    for (int i = 0; i < size - 1; i++) {
        for (int j = i + 1; j < size; j++) {
            if (freq[i] < freq[j]) {
                unsigned temp_freq = freq[i];
                freq[i] = freq[j];
                freq[j] = temp_freq;

                char temp_char = data[i];
                data[i] = data[j];
                data[j] = temp_char;

                char *temp_code = binary_code[i];
                binary_code[i] = binary_code[j];
                binary_code[j] = temp_code;
            }
        }
    }
}

int main(int argc, char **argv) {
    if (argc != 5) {
        printf("Usage: %s encode <input file> <code table file> <encoded text file>\n", argv[0]);
        return 1;
    }

    char *mode = argv[1];
    char *inputFilePath = argv[2];
    char *codeTableFilePath = argv[3];
    char *encodedFilePath = argv[4];

    if (strcmp(mode, "encode") != 0) {
        printf("Invalid mode. Use 'encode'.\n");
        return 1;
    }

    FILE *inputFile = fopen(inputFilePath, "r");
    if (inputFile == NULL) {
        printf("Could not open file to read: %s\n", inputFilePath);
        return 1;
    }

    Code *codeTable = malloc(sizeof(Code) * 256);
    for (int i = 0; i < 256; i++) {
        codeTable[i].freq = 0;
        codeTable[i].binary_code = NULL;
    }

    int totalNumOfCharacters = 0;
    int c;
    while ((c = fgetc(inputFile)) != EOF && c != '\n') {
        codeTable[c].freq++;
        totalNumOfCharacters++;
    }
    fclose(inputFile);

    char data[256];
    unsigned freq[256];
    char *binary_code[256];
    int size = 0;
    for (int i = 0; i < 256; i++) {
        if (codeTable[i].freq > 0) {
            data[size] = (char)i;
            freq[size] = codeTable[i].freq;
            binary_code[size] = codeTable[i].binary_code;
            size++;
        }
    }

    Node *root = build_huffman_tree(data, freq, size);

    char code[256];
    generate_codes(root, code, 0, codeTable);

    for (int i = 0; i < size; i++) {
        binary_code[i] = codeTable[(unsigned char)data[i]].binary_code;
    }

    sort_code_table(data, freq, binary_code, size);

    FILE *codeTableFile = fopen(codeTableFilePath, "w");
    if (codeTableFile == NULL) {
        printf("Could not open file to write: %s\n", codeTableFilePath);
        return 1;
    }

    for (int i = 0; i < size; i++) {
        fprintf(codeTableFile, "%c\t%s\t%d\n", data[i], binary_code[i], freq[i]);
    }
    fclose(codeTableFile);

    FILE *encodedFile = fopen(encodedFilePath, "w");
    if (encodedFile == NULL) {
        printf("Could not open file to write: %s\n", encodedFilePath);
        return 1;
    }

    inputFile = fopen(inputFilePath, "r");
    encode_text(inputFile, encodedFile, codeTable);
    fclose(inputFile);
    fclose(encodedFile);

    int compressed_size = 0;
    for (int i = 0; i < size; i++) {
        compressed_size += freq[i] * strlen(binary_code[i]);
    }

    printf("Original: %d bits\n", totalNumOfCharacters * 8);
    printf("Compressed: %d bits\n", compressed_size);
    printf("Compression Ratio: %.2f%%\n", (float)compressed_size / ((float)totalNumOfCharacters * 8) * 100);

    return 0;
}