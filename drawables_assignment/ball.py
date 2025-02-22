#Andy Vo av827

from drawable import *
import pygame
import math 

class Ball(Drawable):

    def __init__(self, x = 0, y = 0, radius = 10, color = (0, 0, 0)):
        super().__init__(x, y)
        self.__color = color    
        self.__radius = radius 
        self.__xSpeed = 5
        self.__ySpeed = 5

    def draw(self, surface):
        if self.isVisible():
            pygame.draw.circle(surface, self.__color, self. getLoc(), self.__radius)

    def move(self): 
        currentX, currentY = self.getLoc()
        newX = currentX + self.__xSpeed
        newY = currentY + self.__ySpeed
        self.setX(newX)
        self.setY(newY)

        surface = pygame.display.get_surface()
        width, height = surface.get_size()

        if newX <= self.__radius or newX + self.__radius >= width:
            self.__xSpeed *= -1  
        
        if newY <= self.__radius or newY + self.__radius >= height:
            self.__ySpeed *= -1 

    def get_rect(self):
        location = self.getLoc()
        radius = self.__radius 
        return pygame.Rect(location[0] - radius, location[1] - radius, 2 * radius, 2 * radius)
    
    def getColor(self):
        return self.__color

    def setColor(self, color):
        self.__color = color 

    def getRadius(self):
        return self.__radius

    def setRadius(self, radius):
        self.__radius = radius

    def isTouchingBall(self, other): # ???
        distance = math.sqrt((self.getLoc()[0] - other.getLoc()[0]) ** 2 + (self.getLoc()[1] - other.getLoc()[1]) ** 2)
        if distance <= self.getRadius() + other.getRadius():
            return True
        return False

    def getXSpeed(self):
        return self.__xSpeed

    def setXSpeed(self, xMovement):
        self.__xSpeed = xMovement

    def getYSpeed(self):
        return self.__ySpeed 

    def setYSpeed(self, yMovement):
        self.__ySpeed = yMovement

