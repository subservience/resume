#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>

void indexTo2D(int index, int k, int *row, int *col) {
    if (row != NULL) {
        *row = index / k;
    }
    if (col != NULL) {
        *col = index % k;
    }
}

int indexTo1D(int row, int col, int k) {
    return row * k + col;
}

int moveUp(int index, int k) {
    int row, col;
    indexTo2D(index, k, &row, &col);

    if (row < 1) {
        return -1;
    }

    return indexTo1D(row - 1, col, k);
}

int moveDown(int index, int k) {
    int row, col;
    indexTo2D(index, k, &row, &col);

    if (row > k - 2) {
        return -1;
    }

    return indexTo1D(row + 1, col, k);
}

int moveLeft(int index, int k) {
    int row, col;
    indexTo2D(index, k, &row, &col);

    if (col < 1) {
        return -1;
    }

    return indexTo1D(row, col - 1, k);
}

int moveRight(int index, int k) {
    int row, col;
    indexTo2D(index, k, &row, &col);

    if (col > k - 2) {
        return -1;
    }

    return indexTo1D(row, col + 1, k);
}

typedef struct Node {
    int *board;
    int move;
    struct Node *next;
    struct Node *parent;
} Node;

typedef struct HashTable {
    int size;
    Node **table;
} HashTable;

HashTable *createHashTable(int size) {
    HashTable *newTable = (HashTable *)malloc(sizeof(HashTable));
    newTable->size = size;
    newTable->table = (Node **)malloc(size * sizeof(Node *));
    for (int i = 0; i < size; i++) {
        newTable->table[i] = NULL;
    }
    return newTable;
}

unsigned int hashBoard(int *board, int k) {
    unsigned int hashValue = 0;
    for (int i = 0; i < k * k; i++) {
        hashValue = 31 * hashValue + board[i];
    }
    return hashValue;
}

Node *createNode(int *board, int k, int move, Node *parent) {
    Node *newNode = (Node *)malloc(sizeof(Node));
    newNode->board = (int *)malloc(sizeof(int) * k * k);
    memcpy(newNode->board, board, sizeof(int) * k * k);
    newNode->move = move;
    newNode->parent = parent;
    newNode->next = NULL;
    return newNode;
}

bool isEven(int number) {
    return number % 2 == 0;
}

bool isGoalState(int *board, int k) {
    for (int i = 0; i < k * k - 1; i++) {
        if (board[i] != i + 1) {
            return false;
        }
    }
    return board[k * k - 1] == 0;
}

bool isSolvable(int *board, int k) {
    int inversionCount = 0;
    int emptyRow = 0;

    for (int i = 0; i < k * k; i++) {
        if (board[i] == 0) {
            indexTo2D(i, k, &emptyRow, NULL);
            continue;
        }

        for (int j = i + 1; j < k * k; j++) {
            if (board[i] > board[j] && board[j]) {
                inversionCount++;
            }
        }
    }

    if (!isEven(k) && !isEven(inversionCount)) {
        return false;
    }

    if (isEven(k) && isEven(inversionCount + emptyRow)) {
        return false;
    }

    return true;
}

bool isBoardInHashTable(HashTable *table, int *board, int k) {
    unsigned int hashValue = hashBoard(board, k) % table->size;
    Node *currentNode = table->table[hashValue];

    while (currentNode) {
        if (memcmp(currentNode->board, board, sizeof(int) * k * k) == 0) {
            return true;
        }
        currentNode = currentNode->next;
    }

    return false;
}

Node *solvePuzzle(int *initialBoard, int k) {
    Node *queue[100000];
    int queueStart = 0;
    int queueEnd = 0;
    HashTable *visited = createHashTable(100003);

    Node *rootNode = createNode(initialBoard, k, -1, NULL);
    queue[queueEnd++] = rootNode;

    while (queueStart < queueEnd) {
        Node *currentNode = queue[queueStart++];
        if (isGoalState(currentNode->board, k)) {
            return currentNode;
        }

        int emptyIndex;
        for (int i = 0; i < k * k; i++) {
            if (currentNode->board[i] == 0) {
                emptyIndex = i;
            }
        }

        int possibleMoves[4] = {moveUp(emptyIndex, k), moveDown(emptyIndex, k), moveLeft(emptyIndex, k), moveRight(emptyIndex, k)};

        for (int moveIndex = 0; moveIndex < 4; moveIndex++) {
            int newPosition = possibleMoves[moveIndex];
            if (newPosition >= 0 && newPosition < k * k) {
                int *newBoard = (int *)malloc(sizeof(int) * k * k);
                memcpy(newBoard, currentNode->board, sizeof(int) * k * k);
                newBoard[emptyIndex] = newBoard[newPosition];
                newBoard[newPosition] = 0;

                if (!isBoardInHashTable(visited, newBoard, k)) {
                    Node *newNode = createNode(newBoard, k, newBoard[emptyIndex], currentNode);
                    queue[queueEnd++] = newNode;
                    unsigned int hashValue = hashBoard(newBoard, k) % visited->size;
                    newNode->next = visited->table[hashValue];
                    visited->table[hashValue] = newNode;
                }
                free(newBoard);
            }
        }
    }
    return NULL;
}

void reconstructPath(Node *node, FILE *outputFile) {
    int moves[1000];
    int moveCount = 0;
    Node *currentNode = node;

    while (currentNode->parent != NULL) {
        moves[moveCount++] = currentNode->move;
        currentNode = currentNode->parent;
    }

    for (int i = moveCount - 1; i >= 0; i--) {
        fprintf(outputFile, "%d ", moves[i]);
    }

    fprintf(outputFile, "\n");
}

int main(int argc, char **argv) {
    FILE *inputFile, *outputFile;

    inputFile = fopen(argv[1], "r");
    if (inputFile == NULL) {
        printf("Error: Could not open input file %s.\n", argv[1]);
        return -1;
    }

    outputFile = fopen(argv[2], "w");
    if (outputFile == NULL) {
        printf("Error: Could not open output file %s.\n", argv[2]);
        fclose(inputFile);
        return -1;
    }

    char *line = NULL;
    size_t lineBufferSize = 0;
    int boardSize;

    getline(&line, &lineBufferSize, inputFile);
    fscanf(inputFile, "%d\n", &boardSize);
    getline(&line, &lineBufferSize, inputFile);

    int initialBoard[boardSize * boardSize];
    for (int i = 0; i < boardSize * boardSize; i++) {
        fscanf(inputFile, "%d ", &initialBoard[i]);
    }
    fclose(inputFile);

    fprintf(outputFile, "#moves\n");
    if (!isSolvable(initialBoard, boardSize)) {
        fprintf(outputFile, "no solution\n");
    } else {
        Node *solution = solvePuzzle(initialBoard, boardSize);
        reconstructPath(solution, outputFile);
    }

    fclose(outputFile);

    return 0;
}