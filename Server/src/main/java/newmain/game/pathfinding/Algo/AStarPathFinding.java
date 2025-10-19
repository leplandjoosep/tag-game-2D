package newmain.game.pathfinding.Algo;

import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import newmain.game.characters.NewBot;
import newmain.game.characters.PlayerGameCharacter;
import newmain.game.pathfinding.Node;
import newmain.game.world.World;


public class AStarPathFinding {

    private IndexedAStarPathFinder<Node> aStarPathFinder;
    private World world;
    // Pass in the path and the algorithm will fill it with data.
    private SolutionGraphPath solutionGraphPath;
    private ManhattanDistanceHeuristic manhattanDistanceHeuristic;

    // Characters.
    private NewBot bot;
    private PlayerGameCharacter playerGameCharacter;

    /**
     * Class AStarPathFinding constructor.
     *
     * @param world where the Zombie and the PlayerGameCharacter are (World)
     * @param bot instance whose coordinates are used for the start node (Zombie)
     * @param playerGameCharacter instance whose coordinates are used for the end node (PlayerGameCharacter)
     */
    public AStarPathFinding(World world, NewBot bot, PlayerGameCharacter playerGameCharacter) {
        this.world = world;
        this.bot = bot;
        this.playerGameCharacter = playerGameCharacter;
        this.aStarPathFinder = new IndexedAStarPathFinder<Node>(world.getGraph(), false);
        this.solutionGraphPath = new SolutionGraphPath();
        this.manhattanDistanceHeuristic = new ManhattanDistanceHeuristic(world);
    }

    public SolutionGraphPath getSolutionGraphPath() {
        return solutionGraphPath;
    }

    /**
     * Method for calculating a path between start node (Zombie) and end node (PlayerGameCharacter).
     *
     * Method uses GDX-Ai fully implemented A* pathfinder. Calculated path is stored in solutionGraphPath instance.
     */
    public void calculatePath() {
        // Clear graph.
        solutionGraphPath.clear();
        // Zombie x and y.
        int startX = (int) bot.getBoundingBox().getX();
        int startY = (int) bot.getBoundingBox().getY();
        // Target (PlayerGameCharacter) x and y.
        int targetX = (int) playerGameCharacter.getBoundingBox().getX();
        int targetY = (int) playerGameCharacter.getBoundingBox().getY();

        world.getGraph().setWorld(world);
        // Convert Zombie x and y into a node.
        Node startNode = world.getGraph().getNodeByXAndY(startX, startY);
        bot.setCurrentNode(startNode);
        // Convert PlayerGameCharacter x and y into a node:
        Node endNode = world.getGraph().getNodeByXAndY(targetX, targetY);

        // Calculate path. Path is stored in SolutionGraphPath instance.
        aStarPathFinder.searchNodePath(startNode, endNode, manhattanDistanceHeuristic, solutionGraphPath);
    }
}
