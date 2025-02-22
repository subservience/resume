'''
Purpose:        read an integer from keyboard and validate that is a value between lower and upper
Parameters:     lower and upper are two integers to represent lowe and upper bounds
Return value:   a valid integer entered by the user
Sample call:    value = intInRange(0, 100)
'''
def intInRange(lower, upper) :
    stop = False
    while (not stop):
        try:
            number = int(input())
            while(not(number >= lower and number <= upper)):
                  print('Error: you entered a value out of range. Try again.')
                  print('Enter a value between', int(lower), 'and', int(upper), end = ': ')
                  number = int(input())
            stop = True
        except Exception as e:
            print('Invalid input: an integer value was expected. Try again:', end = ' ')
    return number

'''
Purpose:        read a float from keyboard and validate that is a value between lower and upper
Parameters:     lower and upper are two float to represent lowe and upper bounds
Return value:   a valid float entered by the user
Sample call:    value = floatInRange(5.50, 25.00)
'''
def floatInRange(lower, upper) :
    stop = False
    while (not stop):
        try:
            number = float(input())
            while(not(number >= lower and number <= upper)):
                  print('Error: you entered a value out of range. Try again.')
                  print('Enter a value between', float(lower), 'and', float(upper), end = ': ')
                  number = float(input())
            stop = True
        except Exception as e:
            print('Invalid input: a float value was expected. Try again:', end = ' ')
    return number

