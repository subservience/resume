#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define BUFFER_SIZE 256

typedef struct Entry {
    char term[45];
    struct Entry* next;
} Entry;

typedef struct HashMap {
    int capacity;
    Entry** buckets;
} HashMap;

HashMap* createMap(int capacity) {
    HashMap* newMap = (HashMap*)malloc(sizeof(HashMap));
    newMap->capacity = capacity;
    newMap->buckets = (Entry**)malloc(capacity * sizeof(Entry*));
    return newMap;
}

unsigned int computeHash(const char* str) {
    unsigned int value = 0;
    while (*str) {
        value = 31 * value + (unsigned char)(*str++);
    }
    return value;
}

Entry* makeEntry(const char* term) {
    Entry* newEntry = (Entry*)malloc(sizeof(Entry));
    strcpy(newEntry->term, term);
    newEntry->next = NULL;
    return newEntry;
}

void addTerm(HashMap* map, const char* term) {
    unsigned int index = computeHash(term) % map->capacity;
    Entry* newEntry = makeEntry(term);
    newEntry->next = map->buckets[index];
    map->buckets[index] = newEntry;
}

int findTerm(HashMap* map, const char* term) {
    unsigned int index = computeHash(term) % map->capacity;
    Entry* current = map->buckets[index];
    while (current != NULL) {
        if (strcmp(current->term, term) == 0) {
            return 1;
        }
        current = current->next;
    }
    return 0;
}

void suggestAlternatives(HashMap* map, const char* term) {
    printf("Suggestions: ");
    int length = strlen(term);
    char alternative[50];
    strcpy(alternative, term);
    for (int i = 0; i < length - 1; i++) {
        char temp = alternative[i];
        alternative[i] = alternative[i + 1];
        alternative[i + 1] = temp;
        if (findTerm(map, alternative)) {
            printf("%s ", alternative);
        }
        temp = alternative[i];
        alternative[i] = alternative[i + 1];
        alternative[i + 1] = temp;
    }
    for (char c = 'a'; c <= 'z'; c++) {
        snprintf(alternative, sizeof(alternative), "%c%s", c, term);
        if (findTerm(map, alternative)) {
            printf("%s ", alternative);
        }
    }
    for (char c = 'a'; c <= 'z'; c++) {
        snprintf(alternative, sizeof(alternative), "%s%c", term, c);
        if (findTerm(map, alternative)) {
            printf("%s ", alternative);
        }
    }
    strcpy(alternative, term + 1);
    if (findTerm(map, alternative)) {
        printf("%s ", alternative);
    }
    strcpy(alternative, term);
    alternative[length - 1] = '\0';
    if (findTerm(map, alternative)) {
        printf("%s ", alternative);
    }
    printf("\n");
}

void releaseMap(HashMap* map) {
    for (int i = 0; i < map->capacity; i++) {
        Entry* current = map->buckets[i];
        while (current != NULL) {
            Entry* temp = current;
            current = current->next;
            free(temp);
        }
    }
    free(map->buckets);
    free(map);
} 

void analyzeTerm(HashMap* map, const char* term, int* addToMap, int* noErrors) {
    if (findTerm(map, term) == 0) {
        printf("Misspelled word: %s\n", term);
        suggestAlternatives(map, term);
        *noErrors = 0;
        if (*addToMap == 1) {
            addTerm(map, term);
        }
    }
}

int main(int argc, char **argv) {
    char *dictFile = argv[1];
    char *textFile = argv[2];
    char *mode = argv[3];
    int wordCount = 0;
    int addToMap = (strcmp(mode, "add") == 0) ? 1 : 0;
    FILE *fp = fopen(dictFile, "r");
    char *buffer = NULL;
    size_t bufferSize = 0;
    ssize_t bytesRead;
    if (fp == NULL) {
        fprintf(stderr, "Error opening file\n");
        exit(1);
    }
    while ((bytesRead = getline(&buffer, &bufferSize, fp)) != -1) {
        wordCount++;
    }
    HashMap* map = createMap(wordCount);
    fseek(fp, 0, SEEK_SET);
    char term[BUFFER_SIZE];
    for (int i = 0; i < wordCount; i++) {
        fscanf(fp, "%s \n", term);
        addTerm(map, term);
    }
    fclose(fp);
    fp = fopen(textFile, "r");
    if (fp == NULL) {
        fprintf(stderr, "Error opening file\n");
        return -1;
    }
    int noErrors = 1;
    while ((bytesRead = getline(&buffer, &bufferSize, fp)) != -1) {
        char *token;
        const char delimiters[] = " ,.:;!\n";
        token = strtok(buffer, delimiters);
        while (token != NULL) {
            analyzeTerm(map, token, &addToMap, &noErrors);
            token = strtok(NULL, delimiters);
        }
    }
    fclose(fp);
    if (noErrors == 1) printf("No typo!\n");
    releaseMap(map);
    return 0;
}
