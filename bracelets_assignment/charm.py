from abc import ABC, abstractmethod
from enum import Enum

class Charm(ABC):
    
    class Condition(Enum):
        PRISTINE 	= 10 # In perfect condition, as if it were brand new
        EXCELLENT 	= 9  # In very good condition with very little signs of wear and tear
        VERY_GOOD 	= 8  # In good condition with some signs of wear and tear, but still works perfectly
        GOOD			= 7  # In decent condition with noticeable signs of wear and tear, but still works well
        FAIR			= 6  # In usable condition, but may have significant wear and tear or damage
        ACCEPTABLE	= 5  # In somewhat poor condition but still usable or functional
        WORN			= 4  # Well-used and shows signs of wear and tear, but still has some life left in it.
        POOR			= 3	 # In very bad condition, with significant damage or wear and tear, but may still have some function
        DAMAGED		= 2	 # In very poor condition due to damage or abuse; barely usable
        BROKEN		= 1  # Inoperable or non-functional due to damage or malfunction; Repairable
        JUNK			= 0  # Completely unusable or beyond repair, and should be discarded
        
    def __init__(self, name, description, retailPrice, condition):
        self.__name = name
        self.__description = description
        self.__retailPrice = retailPrice
        self.setCondition(condition)
            
    def getName(self):
        return self.__name
    
    def getDescription(self):
        return self.__description
    
    def getRetailPrice(self):
        return self.__retailPrice
    
    def getCondition(self):
        return self.__condition
    
    def setCondition(self, condition):
        if not isinstance(condition, Charm.Condition):
            raise Exception("Condition value must be of type Charm.Condition")
        self.__condition = condition
    
    @abstractmethod
    def getMarketValue(self):
        pass

    @abstractmethod
    def __str__(self):
        pass

    @abstractmethod    
    def __lt__(self, other):
        pass
    
    @abstractmethod
    def __le__(self, other):
        pass
    
    @abstractmethod
    def __gt__(self, other):
        pass

    @abstractmethod
    def __ge__(self, other):
        pass
    
    @abstractmethod
    def __eq__(self, other):
        pass
    
    @abstractmethod
    def __ne__(self, other):
        pass
