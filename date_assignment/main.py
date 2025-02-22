from date import Date #importing our classes
from inputroutines import intInRange

def input_date(): #the function to set the month, day, and year for date1 and date2
    while True:
        try:
            print('Enter Month: ', end = '')
            month = intInRange(1, 12)
            print('Enter Day: ', end = '')
            day = intInRange(1, 31)
            print('Enter Year: ', end = '')
            year = intInRange(1000, 9999)
            return Date(month, day, year) #returns 
        except ValueError:
            print('Invalid Input.')

def main():

    print('Enter Date 1:')
    date1 = input_date() #date1 equals the data that we put into date1 with month, day, year
    print('')
    print('Enter Date 2:')
    date2 = input_date() 
    print('')
    print('Month of Date 1:', date1.getMonth()) #prints out the month of date1
    print('Day of Date 1:', date1.getDay()) #prints out the day of date1
    print('Year of Date 1:', date1.getYear()) #prints out the year of date1
    print('')
    print('Date 1:', date1) #prints out the string of date1
    print('Date 2:', date2) #prints out the string of date2
    print('')
    days_to_add = int(input('Enter number of days to add to Date 1: '))#inputs an ammount of days to be added onto the date
    date1.changeDate(days_to_add)#uses that input and is put into the class to be added to the date1
    print('')
    print('Date 1 after adding days: ', date1)#prints out date 1 after the addition of days
    print('')
    print('Date 1 == Date 2:', date1 == date2)#operands/overloaders
    print('Date 1 != Date 2:', date1 != date2)
    print('Date 1 < Date 2:', date1 < date2)
    print('Date 1 <= Date 2:', date1 <= date2)
    print('Date 1 > Date 2:', date1 > date2)
    print('Date 1 >= Date 2:', date1 >= date2)
    print('')
    print('Month of Date 1:', date1[0])#indexes
    print('Day of Date 1:', date1[1])
    print('Year of Date 1:', date1[2])

if __name__ == "__main__":
    main()

