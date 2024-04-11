# multiAgents.py
# --------------
# Licensing Information:  You are free to use or extend these projects for
# educational purposes provided that (1) you do not distribute or publish
# solutions, (2) you retain this notice, and (3) you provide clear
# attribution to UC Berkeley, including a link to http://ai.berkeley.edu.
# 
# Attribution Information: The Pacman AI projects were developed at UC Berkeley.
# The core projects and autograders were primarily created by John DeNero
# (denero@cs.berkeley.edu) and Dan Klein (klein@cs.berkeley.edu).
# Student side autograding was added by Brad Miller, Nick Hay, and
# Pieter Abbeel (pabbeel@cs.berkeley.edu).


from math import inf
from util import manhattanDistance
from game import Directions
import random, util

from game import Agent
from pacman import GameState

class ReflexAgent(Agent):
    """
    A reflex agent chooses an action at each choice point by examining
    its alternatives via a state evaluation function.

    The code below is provided as a guide.  You are welcome to change
    it in any way you see fit, so long as you don't touch our method
    headers.
    """


    def getAction(self, gameState: GameState):
        """
        You do not need to change this method, but you're welcome to.

        getAction chooses among the best options according to the evaluation function.

        Just like in the previous project, getAction takes a GameState and returns
        some Directions.X for some X in the set {NORTH, SOUTH, WEST, EAST, STOP}
        """
        # Collect legal moves and successor states
        legalMoves = gameState.getLegalActions()

        # Choose one of the best actions
        scores = [self.evaluationFunction(gameState, action) for action in legalMoves]
        bestScore = max(scores)
        bestIndices = [index for index in range(len(scores)) if scores[index] == bestScore]
        chosenIndex = random.choice(bestIndices) # Pick randomly among the best

        "Add more of your code here if you want to"
        legalMoves[chosenIndex]

        return legalMoves[chosenIndex]

    def evaluationFunction(self, currentGameState: GameState, action):
        """
        Design a better evaluation function here.

        The evaluation function takes in the current and proposed successor
        GameStates (pacman.py) and returns a number, where higher numbers are better.

        The code below extracts some useful information from the state, like the
        remaining food (newFood) and Pacman position after moving (newPos).
        newScaredTimes holds the number of moves that each ghost will remain
        scared because of Pacman having eaten a power pellet.

        Print out these variables to see what you're getting, then combine them
        to create a masterful evaluation function.
        """
        # Useful information you can extract from a GameState (pacman.py)
        successorGameState = currentGameState.generatePacmanSuccessor(action)
        newPos = successorGameState.getPacmanPosition()
        newFood = successorGameState.getFood().asList()
        newGhostStates = successorGameState.getGhostStates()
        newScaredTimes = [ghostState.scaredTimer for ghostState in newGhostStates]

        score = successorGameState.getScore()
        # currPos = currentGameState.getPacmanPosition()
        # if opposite to previous action, score penalty (keep track of previous action)
        
        if successorGameState.isWin():
            return inf
        
        if min(newScaredTimes) == 0:
            listghost = [util.manhattanDistance(newPos,ghostPos) for ghostPos in successorGameState.getGhostPositions()]
            distance_ghost = min(listghost)
            if (distance_ghost == 0): #or distance_ghost == 1):
                return -inf
            else:
                score = score - (1.0/distance_ghost)

        listghost = [util.manhattanDistance(newPos,ghostPos) for ghostPos in successorGameState.getGhostPositions()]
        distance_ghost = min(listghost)
        if (distance_ghost == 0 or distance_ghost == 1) and newScaredTimes == 0:
            return -inf
        else:
            score = score + distance_ghost        
        
        if successorGameState.hasFood(newPos[0], newPos[1]):
            score = score + 1000
        listfood = [util.manhattanDistance(newPos,food) for food in newFood]
        distance_food = min(listfood)
        score = score + (1.0/distance_food) - distance_food
        
        if action == Directions.STOP:
            score = score -10
        
        return score

def scoreEvaluationFunction(currentGameState: GameState):
    """
    This default evaluation function just returns the score of the state.
    The score is the same one displayed in the Pacman GUI.

    This evaluation function is meant for use with adversarial search agents
    (not reflex agents).
    """
    return currentGameState.getScore()

class MultiAgentSearchAgent(Agent):
    """
    This class provides some common elements to all of your
    multi-agent searchers.  Any methods defined here will be available
    to the MinimaxPacmanAgent, AlphaBetaPacmanAgent & ExpectimaxPacmanAgent.

    You *do not* need to make any changes here, but you can if you want to
    add functionality to all your adversarial search agents.  Please do not
    remove anything, however.

    Note: this is an abstract class: one that should not be instantiated.  It's
    only partially specified, and designed to be extended.  Agent (game.py)
    is another abstract class.
    """

    def __init__(self, evalFn = 'scoreEvaluationFunction', depth = '2'):
        self.index = 0 # Pacman is always agent index 0
        self.evaluationFunction = util.lookup(evalFn, globals())
        self.depth = int(depth)

class MinimaxAgent(MultiAgentSearchAgent):
    """
    Your minimax agent (question 2)
    """

    def getAction(self, gameState: GameState):
        """
        Returns the minimax action from the current gameState using self.depth
        and self.evaluationFunction.

        Here are some method calls that might be useful when implementing minimax.

        gameState.getLegalActions(agentIndex):
        Returns a list of legal actions for an agent
        agentIndex=0 means Pacman, ghosts are >= 1

        gameState.generateSuccessor(agentIndex, action):
        Returns the successor game state after an agent takes an action

        gameState.getNumAgents():
        Returns the total number of agents in the game

        gameState.isWin():
        Returns whether or not the game state is a winning state

        gameState.isLose():
        Returns whether or not the game state is a losing state
        """
        self.chosen = None
        numberagents = gameState.getNumAgents()
        listagents = list(range(0, numberagents))
        depth = self.depth
        while depth > 1:
            listagents.extend(listagents)
            depth = depth -1
        listagents = listagents + [-1]
        self.listagents = listagents
        self.value(gameState, 0)
        return self.chosen
    
    def value(self, gameState: GameState, index):
        if gameState.isWin() or gameState.isLose():
            return self.evaluationFunction(gameState)

        agentindex = self.listagents[index]
        if agentindex == -1:
            return self.evaluationFunction(gameState)
        elif agentindex == 0:
            return self.max_value(gameState, index)
        else:
            return self.min_value(gameState, index)

            
    def max_value(self, gameState: GameState, index):
        v = -inf
        agentindex = self.listagents[index]
        legalMoves = gameState.getLegalActions(agentindex)
        for action in legalMoves:
            state = gameState.generateSuccessor(agentindex, action)
            new = self.value(state, index+1)
            v = max(v, new)
            if index == 0 and new == v:
                self.chosen = action
        return v
    

    def min_value(self, gameState: GameState, index):
        v = inf
        agentindex = self.listagents[index]
        legalMoves = gameState.getLegalActions(agentindex)
        for action in legalMoves:
            state = gameState.generateSuccessor(agentindex, action)
            v = min(v, self.value(state, index+1))
        return v


        
class AlphaBetaAgent(MultiAgentSearchAgent):
    """
    Your minimax agent with alpha-beta pruning (question 3)
    """

    def getAction(self, gameState: GameState):
        """
        Returns the minimax action using self.depth and self.evaluationFunction
        """
        self.chosen = None
        numberagents = gameState.getNumAgents()
        listagents = list(range(0, numberagents))
        depth = self.depth
        while depth > 1:
            listagents.extend(listagents)
            depth = depth -1
        listagents = listagents + [-1]
        self.listagents = listagents
        a = -inf
        b = inf
        self.value(gameState, 0, a, b)
        return self.chosen
    
    def value(self, gameState: GameState, index, a, b):
        if gameState.isWin() or gameState.isLose():
            return self.evaluationFunction(gameState)
        
        agentindex = self.listagents[index]
        if agentindex == -1:
            return self.evaluationFunction(gameState)
        elif agentindex == 0:
            return self.max_value(gameState, index, a, b)
        else:
            return self.min_value(gameState, index, a, b)

            
    def max_value(self, gameState: GameState, index, a, b):
        v = -inf
        agentindex = self.listagents[index]
        legalMoves = gameState.getLegalActions(agentindex)
        for action in legalMoves:
            state = gameState.generateSuccessor(agentindex, action)
            new = self.value(state, index+1, a, b)
            v = max(v, new)
            if index == 0 and new == v:
                self.chosen = action
            
            if v > b:
                return v
            a = max(a, v)
        return v
    

    def min_value(self, gameState: GameState, index, a, b):
        v = inf
        agentindex = self.listagents[index]
        legalMoves = gameState.getLegalActions(agentindex)
        for action in legalMoves:
            state = gameState.generateSuccessor(agentindex, action)
            v = min(v, self.value(state, index+1, a, b))

            if v < a:
                return v
            b = min(b, v)
        return v


class ExpectimaxAgent(MultiAgentSearchAgent):
    """
      Your expectimax agent (question 4)
    """

    def getAction(self, gameState: GameState):
        """
        Returns the expectimax action using self.depth and self.evaluationFunction

        All ghosts should be modeled as choosing uniformly at random from their
        legal moves.
        """
        
        self.chosen = None
        numberagents = gameState.getNumAgents()
        listagents = list(range(0, numberagents))
        depth = self.depth
        while depth > 1:
            listagents.extend(listagents)
            depth = depth -1
        listagents = listagents + [-1]
        self.listagents = listagents
        self.value(gameState, 0)
        return self.chosen
    
    def value(self, gameState: GameState, index):
        if gameState.isWin() or gameState.isLose():
            return self.evaluationFunction(gameState)

        agentindex = self.listagents[index]
        if agentindex == -1:
            return self.evaluationFunction(gameState)
        elif agentindex == 0:
            return self.max_value(gameState, index)
        else:
            return self.expected_value(gameState, index)

            
    def max_value(self, gameState: GameState, index):
        v = -inf
        agentindex = self.listagents[index]
        legalMoves = gameState.getLegalActions(agentindex)
        for action in legalMoves:
            state = gameState.generateSuccessor(agentindex, action)
            new = self.value(state, index+1)
            v = max(v, new)
            if index == 0 and new == v:
                self.chosen = action
        return v
    

    def expected_value(self, gameState: GameState, index):
        score = 0
        agentindex = self.listagents[index]
        legalMoves = gameState.getLegalActions(agentindex)
        for action in legalMoves:
            state = gameState.generateSuccessor(agentindex, action)
            score = score + self.value(state, index+1)
        return score/len(legalMoves)




def betterEvaluationFunction(currentGameState: GameState):
    """
    Your extreme ghost-hunting, pellet-nabbing, food-gobbling, unstoppable
    evaluation function (question 5).

    DESCRIPTION: <write something here so we know what you did>
    """
    newPos = currentGameState.getPacmanPosition()
    newFood = currentGameState.getFood().asList()
    newGhostStates = currentGameState.getGhostStates()
    newScaredTimes = [ghostState.scaredTimer for ghostState in newGhostStates]

    score = currentGameState.getScore()
    # currPos = currentGameState.getPacmanPosition()
    # if opposite to previous action, score penalty (keep track of previous action)
        
    if currentGameState.isWin():
        return inf
        
    if min(newScaredTimes) == 0:
        listghost = [util.manhattanDistance(newPos,ghostPos) for ghostPos in currentGameState.getGhostPositions()]
        distance_ghost = min(listghost)
        if (distance_ghost == 0): #or distance_ghost == 1):
            return -inf
        else:
            score = score - (1.0/distance_ghost)

    listghost = [util.manhattanDistance(newPos,ghostPos) for ghostPos in currentGameState.getGhostPositions()]
    distance_ghost = min(listghost)
    if (distance_ghost == 0 or distance_ghost == 1) and newScaredTimes == 0:
        return -inf
    else:
        score = score + distance_ghost        
        
    if currentGameState.hasFood(newPos[0], newPos[1]):
        score = score + 1000
    listfood = [util.manhattanDistance(newPos,food) for food in newFood]
    distance_food = min(listfood)
    score = score + (1.0/distance_food) - distance_food
        
    return score


# Abbreviation
better = betterEvaluationFunction
