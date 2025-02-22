# Andy Vo CS172 Homework 5 

from charm import Charm # import charm 

class CollectibleCharm(Charm): # sets charm inside CollectibleCharm
    def __init__(self, name, description, retailPrice, condition, serialNumber): # sets parameters
        super().__init__(name, description, retailPrice, condition) # stores values inside Charm constructor 
        self.__serialNumber = serialNumber # sets serial number 
    
    def getSerialNumber(self):
        return self.__serialNumber # gets serial number
    
    def getMarketValue(self):
        return round(self.getRetailPrice() * (self.getCondition().value / 100), 2)  # gets the market value multiplied by the condition divided by 100 rounded to 2 decimal places
    
    def __str__(self):
        return f'{self.getName()} [{self.getSerialNumber()}]' # string of name ( space ) and the serial number
    
    def __lt__(self, other):
        return self.getMarketValue() < other.getMarketValue() # compares value of current collectible charm and other collectible charm
    
    def __le__(self, other):
        return self.getMarketValue() <= other.getMarketValue() # compares value of current collectible charm and other collectible charm
    
    def __gt__(self, other):
        return self.getMarketValue() > other.getMarketValue() # compares value of current collectible charm and other collectible charm
    
    def __ge__(self, other):
        return self.getMarketValue() >= other.getMarketValue() # compares value of current collectible charm and other collectible charm
        
    def __eq__(self, other):
        return (self.getName() == other.getName() and self.getDescription() == other.getDescription() and self.getRetailPrice() == other.getRetailPrice() and self.getCondition() == other.getCondition() and self.getSerialNumber() == other.getSerialNumber()) # compares all five initial parameters of name, description, retail price, condition, and serial number, if all are equal to each other then return true 
    
    def __ne__(self, other):
        return not self.__eq__(other) # opposite of __eq__
