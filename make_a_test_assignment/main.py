#Andy Vo
#4/29

from questions import *

def answerQuestion(question):
    print(question.displayForTest())
    return input("Enter your answer: ")

def gradeQuestion(question, userAnswer):
    correctAnswer = question.getCorrectAnswer().lower() #turns the correct answer to all lower
    userAnswer = userAnswer.lower() #turns the user answer to all lower case
    if userAnswer == correctAnswer: #if the user answer is the same as the correct answer then call the function to get the points
        return question.getPoints()
    else:
        return 0 #if not, give them 0 points
    
if __name__ == "__main__": #questions
    question1 = MultipleChoice("What is 4 + 51 ?", ['a. 54', 'b. 58', 'c. 55', 'd. 71'], "c", 5)
    question2 = MultipleChoice("What are the pink trees that blossom during the spring ?", ['a. cherry blossom tree', 'b. pine tree', 'c. oak tree', 'd. birch tree'], "a", 5)
    question3 = ShortAnswer("What is the color of black and white when you mix them ?", 4, "gray", 5)
    question4 = ShortAnswer("What is the color of red and blue when you mix them ?", 6, "purple", 5)
    question5 = FillInTheBlank("The coding language we use in CS 171 and 172 is _____.", "python", 5)
    question6 = FillInTheBlank("The main topic in CS 172 is _____.", "classes", 5)

    totalPoints = 0 #initialized
    questions = [question1, question2, question3, question4, question5, question6]
    for question in questions: 
        userAnswer = answerQuestion(question) #runs through function 
        points = gradeQuestion(question, userAnswer) #runs through function
        totalPoints += points #total points

    print(f"You earned: {totalPoints} points")
