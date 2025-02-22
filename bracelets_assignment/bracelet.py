# Andy Vo CS172 Homework 5

from linked_list import LinkedList # imports linked list 
from charm import Charm # imports charm 

class Bracelet(LinkedList): # sets linked list inside of Bracelet
    def __init__(self, base_value): # parameter of base value of the bracelet 
        super().__init__()
        self.__base_value = base_value 

    def append(self, data): # given from the homework PDF
        if isinstance(data, Charm):
            super().append(data)
    
    def appraise(self): 
        total_value = self.__base_value # setting total_value to the base value so we can keep adding onto it 
        current = self.getHead() # sets the current at the start of the bracelet which is the head
        while current is not None: # while current is not none meaning that if it is not empty
            total_value += current.getData().getMarketValue() # gets the data of the node then gets the market value of that node 
            current = current.getNext() # changes current to the next node 
            if self.isClosed() and current == self.getHead(): # if the bracelet is closed and if the current is the same as the head, then break the function
                break
        return round(total_value, 2) # return the total value of the bracelet with 2 decimal places 
    
    def close(self): 
        if len(self) > 0 and not self.isClosed(): # if the number of nodes is more than 0 and if it is not closed then run this function
            current = self.getHead() # current is set to the head of the node 
            while current.getNext() is not None and current != self.getHead(): # while the next node after the current node is not none then continue 
                current = current.getNext() # make current the next node
            current.setNext(self.getHead()) # if current is none then set next node as the head therefore closing the bracelet
    
    def open(self): 
        if self.isClosed(): # if bracelet is closed then run the function
            current = self.getHead() # current is set to the head 
            while current.getNext() != self.getHead(): # while the next node after the current is not equal to the head of the node then continue
                current = current.getNext() # current is set to next node
            current.setNext(None) # if the node after current is equal to the head of the node then set the next node to none
    
    def isClosed(self): 
        if self.isEmpty(): # if the bracelet is empty return False
            return False
        current = self.getHead() # current is set to the ehad
        while current.getNext() is not None: # if the next node is not None and if the next node is not the head, then do the following
            if current.getNext() == self.getHead(): 
                return True 
            current = current.getNext()
        return False # returns False
    
    def isOpen(self):
        return not self.isClosed() # opposite of isClosed
