#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define BUFSIZE 256

typedef struct item{
  char *word;
  int weight;
}Item;

Item** searchEntries(Item dict[], int n, char *q, int *matchesCount);
void quickSort(Item dict[], int l, int r);
void arrangeMatches(Item **matches, int resCount, int beg);

void arrangeMatches(Item **matches, int resCount, int beg) {
    if (beg >= resCount - 1) return;

    int end = beg;
    for (int i = beg + 1; i < resCount; i++) {
        if (matches[i]->weight > matches[end]->weight) {
            end = i;
        }
    }

    if (end != beg) {
        Item *temp = matches[beg];
        matches[beg] = matches[end];
        matches[end] = temp;
    }

    arrangeMatches(matches, resCount, beg + 1);
}

void merge(Item dict[], int l, int mid, int r) {
    int n1 = mid - l + 1;
    int n2 = r - mid;

    Item leftArr[n1], rightArr[n2];

    for (int i = 0; i < n1; i++)
        leftArr[i] = dict[l + i];
    for (int j = 0; j < n2; j++)
        rightArr[j] = dict[mid + 1 + j];

    int i = 0, j = 0, k = l;

    while (i < n1 && j < n2) {
        if (strcmp(leftArr[i].word, rightArr[j].word) <= 0) {
            dict[k] = leftArr[i];
            i++;
        } else {
            dict[k] = rightArr[j];
            j++;
        }
        k++;
    }

    while (i < n1) {
        dict[k] = leftArr[i];
        i++;
        k++;
    }

    while (j < n2) {
        dict[k] = rightArr[j];
        j++;
        k++;
    }
}


void quickSort(Item dict[], int l, int r) {
    if (l < r) {
        int mid = l + (r - l) / 2;
        quickSort(dict, l, mid);
        quickSort(dict, mid + 1, r);
        merge(dict, l, mid, r);
    }
}



int locateLeft(Item dict[], int l, int r, char *q, int res) {
    if (l > r) return res;
    int mid = l + (r - l) / 2;
    if (strncmp(dict[mid].word, q, strlen(q)) >= 0) {
        if (strncmp(dict[mid].word, q, strlen(q)) == 0) {
            res = mid;
        }
        return locateLeft(dict, l, mid - 1, q, res);
    } else {
        return locateLeft(dict, mid + 1, r, q, res);
    }
}



int locateRight(Item dict[], int l, int r, char *q, int res) {
    if (l > r) return res;
    int mid = l + (r - l) / 2;
    if (strncmp(dict[mid].word, q, strlen(q)) <= 0) {
        if (strncmp(dict[mid].word, q, strlen(q)) == 0) {
            res = mid;
        }
        return locateRight(dict, mid + 1, r, q, res);
    } else {
        return locateRight(dict, l, mid - 1, q, res);
    }
}


Item** searchEntries(Item dict[], int n, char *q, int *matchesCount) {

    int start = locateLeft(dict, 0, n - 1, q, -1);
    int end = locateRight(dict, 0, n - 1, q, -1);
    
    *matchesCount = end - start + 1;


    Item **matches = (Item **)malloc(*matchesCount * sizeof(Item *));
    
    if (start == -1 || end == -1) {
	*matchesCount = 0;
        printf("No suggestion!\n");
	return NULL;
    } 
    

    for (int i = start; i <= end; i++) {
	matches[i - start] = &dict[i];
    }
    
	
    return matches;
}


int main(int argc, char **argv) {
    char *dictionaryFilePath = argv[1]; 
    char *queryFilePath = argv[2]; 
    int wordCount = 0; 
    int queryCount = 0; 
    int matchesCount = 0;

    Item **matches;
    FILE *fp = fopen(dictionaryFilePath, "r");
    char *line = NULL; 
    size_t lineBuffSize = 0; 
    ssize_t lineSize; 
    
    
    if(fp == NULL){
        fprintf(stderr, "Error opening file:%s\n",dictionaryFilePath);
        return -1;
    }

    
    while((lineSize = getline(&line,&lineBuffSize,fp)) !=-1)
    {
        wordCount++;
    }
    
    Item *dictionary = (Item *)malloc(wordCount * sizeof(Item));

    
    fseek(fp, 0, SEEK_SET);
    char word[BUFSIZE]; 
    int weight;
    for(int i = 0; i < wordCount; i++)
    {
        fscanf(fp, "%s %d\n",word,&weight);
        
        

        dictionary[i].word = (char *)malloc(BUFSIZE * sizeof(char));
	strcpy(dictionary[i].word, word);
	
	dictionary[i].weight = weight;
    }
    
    fclose(fp);

    quickSort(dictionary, 0, wordCount - 1);
    fp = fopen(queryFilePath, "r");
        
    
    if(fp == NULL){
        fprintf(stderr, "Error opening file:%s\n",queryFilePath);
        return -1;
    }

    
    while((lineSize = getline(&line,&lineBuffSize,fp)) !=-1)
    {
        queryCount++;
    }
    free(line); 

    fseek(fp, 0, SEEK_SET);
    for(int i = 0; i < queryCount; i++)
    {
        fscanf(fp, "%s\n",word);
        printf("Query word:%s\n",word);

        matches = searchEntries(dictionary, wordCount, word, &matchesCount);
        
        if (matchesCount > 0) {
            arrangeMatches(matches, matchesCount, 0);

            if (matchesCount > 10) {
                matchesCount = 10;
            }

            for (int j = 0; j < matchesCount; j++) {
                printf("%s %d\n", matches[j]->word, matches[j]->weight);
            }

            free(matches);
        }
    }
    fclose(fp);
    
    for (int i = 0; i < wordCount; i++) {
        free(dictionary[i].word);  
    }

    free(dictionary);


    return 0;
}