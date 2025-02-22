from abc import ABC, abstractmethod #import abstract

class Question(ABC): #Parent Class 

    def __init__(self, prompt, correctanswer = '', points = 0): #setting values
        self.__prompt = prompt #String
        self.__correctAnswer = correctanswer #String
        self.__points = points #Integer

    def getPrompt(self): #getter
        return self.__prompt
    
    def getCorrectAnswer(self): #getter
        return self.__correctAnswer
    
    def getPoints(self): #getter
        return self.__points

    def setPrompt(self, prompt): #setter
        self.__prompt = prompt

    def setCorrectAnswer(self, correctanswer): #setter
        self.__correctAnswer = correctanswer

    def setPoints(self, points):#setter
        self.__points = points

    @abstractmethod 
    def displayForTest(self): #pass since its just the parent function, nothing to really test here
        pass

    def __str__(self): #__str__ testing for __str__
        return f"Prompt: {self.__prompt}\nCorrect Answer: {self.__correctAnswer}\nPoints: {self.__points}"
    
class MultipleChoice(Question): #Child Class

    def __init__(self, prompt, choices, correctanswer = '', points = 0): #init things
        super().__init__(prompt, correctanswer, points)
        self.__choices = choices

    def addChoice(self, choice): #appends choice to the list of choices
        self.__choices.append(choice)

    def updateChoice(self, index, choice): #setter
        self.__choices[index] = choice
    
    def getChoices(self): #getter
        return self.__choices
    
    def displayForTest(self): #runs test
        output = f"{self.getPrompt()}\n" #first output is the prompt 
        for choice in self.__choices: #add the choice with the output then return output
            output += f"{choice}\n"
        return output
    
    def __str__(self): #__str__ overloader
        return f"{super().__str__()}\nChoices: {self.__choices}"

class ShortAnswer(Question): #Child Class

    def __init__(self, prompt, length, correctanswer = '', points = 0): 
        super().__init__(prompt, correctanswer, points)
        self.__length = length

    def getLength(self): #getter
        return self.__length
    
    def setLength(self, length): #setter
        self.__length = length
        
    def displayForTest(self): #test
        return f"{self.getPrompt()} (up to {self.__length} characters)"
    
    def __str__(self): #__str__ overloader
        return f"{super().__str__()}\nCharacter limit: {self.__length}"

class FillInTheBlank(Question): #Child Class
        
    def __init__(self, prompt, correctanswer = '', points = 0):
        super().__init__(prompt, correctanswer, points)

    def displayForTest(self): #test
        return f"Fill in the blank:\n{self.getPrompt()}"

    def __str__(self): #__str__ overloader
        return f"{super().__str__()}\n"

