from charm import Charm # imports charm 

class FriendshipCharm(Charm): # sets charm inside FriendshipCharm
    def __init__(self, name, description, retailPrice, condition, symbol): # sets parameters
        super().__init__(name, description, retailPrice, condition) # stores values inside Charm constructor 
        self.__symbol = symbol # sets symbol
    
    def getMarketValue(self):
        return round(self.getCondition().value / 100, 2) # gets the market value divided by 100 rounded to 2 decimal places
    
    def getSymbol(self):
        return self.__symbol # returns symbol
    
    def __str__(self):
        return self.__symbol # string
    
    def __lt__(self, other):
        return self.getMarketValue() < other.getMarketValue() # compares value of current friendship charm and other friendship charm 

    def __le__(self, other):
        return self.getMarketValue() <= other.getMarketValue() # compares value of current friendship charm and other friendship charm 

    def __gt__(self, other):
        return self.getMarketValue() > other.getMarketValue() # compares value of current friendship charm and other friendship charm 

    def __ge__(self, other):
        return self.getMarketValue() >= other.getMarketValue() # compares value of current friendship charm and other friendship charm 

    def __eq__(self, other):
        return self.getMarketValue() == other.getMarketValue() # compares value of current friendship charm and other friendship charm

    def __ne__(self, other):
        return self.getMarketValue() != other.getMarketValue() # compares value of current friendship charm and other friendship charm
