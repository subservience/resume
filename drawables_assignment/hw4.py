#Andy Vo av827

import pygame 
from drawable import *
from ball import *
from paddle import * 
from text import * 
from random import * 

pygame.init()
surface = pygame.display.set_mode((800, 600))
DREXEL_BLUE = (7, 41, 77)
myBall = Ball(400, 300, 25, DREXEL_BLUE)
myPaddle = Paddle(200, 25, DREXEL_BLUE)
myScoreBoard = Text("Score: 0", 10, 10)
numHits = 0 
running = True
fpsClock = pygame.time.Clock()
gameLossCondition = False 
gameWinCondition = False

while running: 
    surface.fill((255, 255, 255))
    myBall.draw(surface)
    myPaddle.draw(surface)
    myScoreBoard.draw(surface)

    if myBall.intersects(myPaddle):
        myBall.setYSpeed(myBall.getYSpeed()*-1)
        numHits += 1 
        myScoreBoard.setMessage("Score: " + str(numHits))
        myBall.setXSpeed(myBall.getXSpeed() + 0.25)
        myBall.setYSpeed(myBall.getYSpeed() + 0.25)
        myPaddle.setWidth(randint(100,500))

    myBall.move()

    if myBall.getLoc()[1] + myBall.getRadius() >= surface.get_height():
        myBall.setXSpeed(5)
        myBall.setYSpeed(5)
        numHits -= 1
        myScoreBoard.setMessage("Score: " + str(numHits))

    if numHits >= 3: 
        gameWinCondition = True    
        
    if numHits < 0: 
        gameLossCondition = True

    if gameWinCondition: 
        myScoreBoard.setMessage("You Have Won ! Current Score: " + str(numHits))

    if gameLossCondition:
        myScoreBoard.setMessage("You Have Lost ! Current Score: " + str(numHits))
        

    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            running = False
            pygame.quit()
            exit()
    pygame.display.update()
    fpsClock.tick(30)

