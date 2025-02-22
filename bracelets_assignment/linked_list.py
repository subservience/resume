#File:     linked_list.py
#Purpose:  Demo on how to create and use a link list
#Authors:  Adelaida A. Medlock, Daniel Moix
#Created:  February 19, 2021
#Revised:  April 14, 2023

# The Node class - to be used to create linked lists
# a Node is the basic unit in a linked list
class Node():
    # constructor
    def __init__(self, data, next = None):
        self.__data = data
        self.__next = next
    
    # getters
    def getData(self):
        return self.__data
    
    def getNext(self):
        return self.__next
    
    #setters
    def setData(self, d):
        self.__data = d

    def setNext(self, n):
        self.__next = n

    #overloaded operator
    def __str__(self):
        return str(self.__data) + " whose next node is " + str(self.__next)


# The LinkedList class: a collection of nodes
# where a node is: data and a link to the next node
# the data in the Node can be any simple data type or objects we create
class LinkedList():
    # constructor
    def __init__(self):
        self.__head = None
        
    # getter: access to the head node
    def getHead(self):
        return self.__head
    
    # getter: check if list is empty
    def isEmpty(self):
        return self.__head == None
    
    # setter: add a node at the end of the linked list
    def append(self, data):
        newNode = Node(data)
        
        if self.isEmpty():       # if list is empty, head will point to newNode
            self.__head = newNode
            
        else:                     # list is not empty, go to end of list and add newNode there
            current = self.__head
            while current.getNext() != None: # check if theres's another item after the current node
                current = current.getNext()
            current.setNext(newNode)
            
    # setter: remove a node from the linked list
    # returns a Boolean indicating if the operation was successful or not
    def remove(self, item):
        current = self.__head
        previous = None
        found = False
        
        # first find item in the list
        while not found and current != None :
            if current.getData() == item:
                found = True
            else:
                previous = current
                current = current.getNext()
        
        if current == None:  #list was empty or item was not in the list
            return False
        elif previous == None:  # item was in the fist node
            self.__head = current.getNext()
            return True
        else:  # item was somewhere after the first node
            previous.setNext(current.getNext())
            return True

    
    # search for item in linked list
    # returns a Boolean indicating if the item was found or not
    # other version could return 'index' or location of item
    def search(self, item):
        current = self.__head
        found = False
        while current != None and not found:
            if current.getData() == item:
                found = True
            else:
                current = current.getNext()
        return found
    
    #overloaded operators
    # used to suppport []
    def __getitem__(self, index):  
        if index < 0 or index > len(self) - 1:
            raise IndexError
        
        current = self.__head
        for i in range(index):
            current = current.getNext()
        return current.getData()
    
    # used to support print(myLinkedList)
    def __str__(self):    
        mystr = ''
        current = self.__head
            
        while current != None:
            mystr += str(current.getData()) + ' --> '
            current = current.getNext()
        
        return mystr
    
    # used to support len(myLinkedList)
    def __len__(self):    
        if self.__head == None:  # if list is empty return 0
            return 0
        
        current = self.__head   #list is not empty and has at least 1 Node
        counter = 1
        
        while current.getNext() != None: # check if theres's another item after the current node
            counter += 1
            current = current.getNext()
        return counter

