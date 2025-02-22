from inputroutines import intInRange

class Date:
    def __init__(self, month=1, day=1, year=1900):#initialized values
        if not self._is_valid_date(month, day, year):#if it doesnt match up with the dictionary in valud date then prints out invalid input and asks the user to try again
            print('Invalid Input.')
        self.__month = month
        self.__day = day
        self.__year = year

    def _is_valid_date(self, month, day, year):#dictionary, checks if month lines up with the day in the month
        days_in_month = {
            1: 31,  # January
            2: 28,  # February
            3: 31,  # March
            4: 30,  # April
            5: 31,  # May
            6: 30,  # June
            7: 31,  # July
            8: 31,  # August
            9: 30,  # September
            10: 31,  # October
            11: 30,  # November
            12: 31  # December
        }
        if month < 1 or month > 12:#checks values of month 
            return False
        if day < 1 or day > days_in_month[month]:#checks values of day
            return False
        return True#returns true if passes all cases

    def getMonth(self):
        return self.__month#returns month 

    def getDay(self):
        return self.__day#returns day

    def getYear(self):
        return self.__year#returns year

    def changeDate(self, days):#changes the date based on the amount of days added
        while days > 0:#when the day inserted is greater than 0 run the following
            days_in_current_month = self._days_in_month(self.__month)#gets the amount of days in the month
            if self.__day + days <= days_in_current_month:#if the addition in the days is less than what the max is allowed in the month, then simply add the days
                self.__day += days
                break
            else:
                days -= (days_in_current_month - self.__day + 1)
                self.__day = 1#set day to 1 
                self.__month += 1#add 1 to month
                if self.__month > 12:#if months ends up being more than 12
                    self.__month = 1#set month to 1
                    self.__year += 1#add 1 to year

    def _days_in_month(self, month):#dictionary
        days_in_month = {
            1: 31,  # January
            2: 28,  # February
            3: 31,  # March
            4: 30,  # April
            5: 31,  # May
            6: 30,  # June
            7: 31,  # July
            8: 31,  # August
            9: 30,  # September
            10: 31,  # October
            11: 30,  # November
            12: 31  # December
        }
        return days_in_month[month]

    def __str__(self):
        return f"{self.__month:02d}/{self.__day:02d}/{self.__year}"

    def __eq__(self, other):#equal
        return (self.__year, self.__month, self.__day) == (other.__year, other.__month, other.__day)

    def __ne__(self, other):#not equal
        return not self.__eq__(other)

    def __lt__(self, other):#less than
        return (self.__year, self.__month, self.__day) < (other.__year, other.__month, other.__day)

    def __le__(self, other):#less than or equal to
        return self.__eq__(other) or self.__lt__(other)

    def __gt__(self, other):#greater than 
        return not self.__le__(other)

    def __ge__(self, other):#greater than or equal to
        return not self.__lt__(other)

    def __getitem__(self, index):#indexes
        if index == 0:
            return self.__month
        elif index == 1:
            return self.__day
        elif index == 2:
            return self.__year
        else:
            raise IndexError('Index out of Bounds')
