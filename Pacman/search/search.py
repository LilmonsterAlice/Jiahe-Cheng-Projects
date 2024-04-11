# search.py
# ---------
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


"""
In search.py, you will implement generic search algorithms which are called by
Pacman agents (in searchAgents.py).
"""

import util

class SearchProblem:
    """
    This class outlines the structure of a search problem, but doesn't implement
    any of the methods (in object-oriented terminology: an abstract class).

    You do not need to change anything in this class, ever.
    """

    def getStartState(self):
        """
        Returns the start state for the search problem.
        """
        util.raiseNotDefined()

    def isGoalState(self, state):
        """
          state: Search state

        Returns True if and only if the state is a valid goal state.
        """
        util.raiseNotDefined()

    def getSuccessors(self, state):
        """
          state: Search state

        For a given state, this should return a list of triples, (successor,
        action, stepCost), where 'successor' is a successor to the current
        state, 'action' is the action required to get there, and 'stepCost' is
        the incremental cost of expanding to that successor.
        """
        util.raiseNotDefined()

    def getCostOfActions(self, actions):
        """
         actions: A list of actions to take

        This method returns the total cost of a particular sequence of actions.
        The sequence must be composed of legal moves.
        """
        util.raiseNotDefined()


def tinyMazeSearch(problem):
    """
    Returns a sequence of moves that solves tinyMaze.  For any other maze, the
    sequence of moves will be incorrect, so only use this for tinyMaze.
    """
    from game import Directions
    s = Directions.SOUTH
    w = Directions.WEST
    return  [s, s, w, s, w, w, s, w]

def depthFirstSearch(problem: SearchProblem):
    """
    Search the deepest nodes in the search tree first.

    Your search algorithm needs to return a list of actions that reaches the
    goal. Make sure to implement a graph search algorithm.

    To get started, you might want to try some of these simple commands to
    understand the search problem that is being passed in:

    print("Start:", problem.getStartState())
    print("Is the start a goal?", problem.isGoalState(problem.getStartState()))
    print("Start's successors:", problem.getSuccessors(problem.getStartState()))
    """

    start = problem.getStartState()
    marked = [] #marked nodes
    path = [] #list of actions
    stack = util.Stack() #queued nodes, have ([node, path])
    stack.push([start, []])

    while not stack.isEmpty():
        leaf = stack.pop()
        node = leaf[0]
        path = leaf[1] #path is str??? how to fix it??? but I already initialized it to []
        marked.append(node)
        if (problem.isGoalState(node)):
            return path
        else:
            for i in problem.getSuccessors(node):
                if i[0] not in marked:
                    stack.push([i[0], path + [i[1]]])



def breadthFirstSearch(problem: SearchProblem):
    """Search the shallowest nodes in the search tree first."""
    
    start = problem.getStartState()
    marked = [] #marked nodes
    marked.append(start)
    path = [] #list of actions
    queue = util.Queue() #queued nodes, have ([node, path])
    queue.push([start, []])

    while not queue.isEmpty():
        leaf = queue.pop()
        node = leaf[0]
        path = leaf[1]
        if (problem.isGoalState(node)):
            return path
        else:
            for i in problem.getSuccessors(node):
                if i[0] not in marked:
                    marked.append(i[0])
                    queue.push([i[0], path + [i[1]]])


def uniformCostSearch(problem: SearchProblem):
    """Search the node of least total cost first."""
    
    start = problem.getStartState()
    marked = [] #marked nodes
    path = [] #list of actions
    pqueue = util.PriorityQueue() #queued nodes, have ([node, path, distance], distance)
    pqueue.push([start, [], 0], 0)

    while not pqueue.isEmpty():
        leaf = pqueue.pop()
        node = leaf[0]
        if node not in marked:
            marked.append(node)
            path = leaf[1]
            distance = leaf[2]
            if (problem.isGoalState(node)):
                return path
            else:
                for i in problem.getSuccessors(node):
                        pqueue.update([i[0], path + [i[1]], distance + i[2]], distance + i[2])


def nullHeuristic(state, problem=None):
    """
    A heuristic function estimates the cost from the current state to the nearest
    goal in the provided SearchProblem.  This heuristic is trivial.
    """
    return 0

def aStarSearch(problem: SearchProblem, heuristic=nullHeuristic):
    """Search the node that has the lowest combined cost and heuristic first."""

    start = problem.getStartState()
    marked = [] #marked nodes
    path = [] #list of actions
    pqueue = util.PriorityQueue() #queued nodes, have ([node, path, distance], hdistance)
    pqueue.push([start, [], 0], 0 + heuristic(start, problem))

    while not pqueue.isEmpty():
        leaf = pqueue.pop()
        node = leaf[0]
        if node not in marked:
            marked.append(node)
            path = leaf[1]
            distance = leaf[2]
            if (problem.isGoalState(node)):
                return path
            else:
                for i in problem.getSuccessors(node):
                        pqueue.update([i[0], path + [i[1]], distance + i[2]], distance + i[2] + heuristic(i[0], problem))



# Abbreviations
bfs = breadthFirstSearch
dfs = depthFirstSearch
astar = aStarSearch
ucs = uniformCostSearch
