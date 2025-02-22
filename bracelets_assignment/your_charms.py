# Andy Vo CS172 Homework 5 
from charm import Charm

class KyraCharm(Charm):
    def __init__(self, name, description, retailPrice, condition, dice):
        super().__init__(name, description, retailPrice, condition)
        self.__dice = dice

    def getDice(self):
        return self.__dice
    
    def getMarketValue(self):
        return round(self.__dice * self.getCondition().value / 100, 2)
    
    def roll(self):
        while self.__dice > 20:
            self.__dice - 20 
            if self.__dice == 1: 
                return f'{self.getName} has rolled a NAT 1 !'
            elif self.__dice == 20: 
                return f'{self.getName} has rolled a NAT 20 !'
            else: 
                return f'{self.getName} has rolled a {self.__dice}'
    
    def __str__(self):
        return f'{self.getName()} Rolled a Dice of {self.__dice}'
    
    def __lt__(self, other):
        return self.__dice() < other.__dice() # compares value of current dice and other dice 
    
    def __le__(self, other):
        return self.__dice() <= other.__dice() # compares value of current dice and other dice 
    
    def __gt__(self, other):
        return self.__dice() > other.__dice() # compares value of current dice and other dice 
    
    def __ge__(self, other):
        return self.__dice() >= other.__dice() # compares value of current dice and other dice 
        
    def __eq__(self, other):
        return self.__dice() > other.__dice() # checks if current dice and other dice are equal
    
    def __ne__(self, other):
        return not self.__eq__(other) # opposite of __eq__
