package problems;

import datastructures.IntTree;


// IntelliJ will complain that this is an unused import, but you should use IntTreeNode variables
// in your solution, and then this error should go away.
import datastructures.IntTree.IntTreeNode;

/**
 * Parts b.vi, through b.x should go here.
 *
 * (Implement depthSum, numberNodes, removeLeaves, tighten, and trim as described by the spec.
 * See the spec on the website for picture examples and more explanation!)
 *
 * Also note: you may want to use private helper methods to help you solve these problems.
 * YOU MUST MAKE THE PRIVATE HELPER METHODS STATIC, or else your code will not compile.
 * This happens for reasons that aren't the focus of this assignment and are mostly skimmed over in 142
 * and 143.  If you want to know more you can ask on Piazza or at office hours.
 *
 * REMEMBER THE FOLLOWING RESTRICTIONS:
 * - do not construct new IntTreeNode objects (though you may have as many IntTreeNode variables as you like).
 * - do not call any IntTree methods
 * - do not construct any external data structures like arrays, queues, lists, etc.
 * - do not mutate the .data field of any nodes (except for numberNodes),
 */

public class IntTreeProblems {

    public static int depthSum(IntTree tree) {
        return depthSum(tree.overallRoot, 1);
    }
    private static int depthSum(IntTreeNode root, int l)
    {
        if (root == null)
        {
            return 0;
        }
        else
        {
            return l * root.data + depthSum(root.left, l + 1) + depthSum(root.right, l + 1);
        }
    }


    public static void removeLeaves(IntTree tree) {
        tree.overallRoot= removeLeaves(tree.overallRoot);
    }
    private static IntTreeNode removeLeaves(IntTreeNode root) {
        if (root == null || (root.left == null && root.right == null)) {
            return null;
        } else {
            root.left = removeLeaves(root.left);
            root.right = removeLeaves(root.right);
            return root;
        }
    }

    public static int numberNodes(IntTree tree) {
        return numberNodes(tree.overallRoot, 1);
    }
    private static int numberNodes(IntTreeNode root, int counter)
    {
        if (root == null)
        {
            return 0;
        }
        root.data = counter;
        int leftc=numberNodes(root.left, 1+counter);
        int rightc=numberNodes(root.right, 1+counter+leftc);

        return 1+ leftc + rightc;
    }

    public static void tighten(IntTree tree) {
        tree.overallRoot=tighten(tree.overallRoot);
    }
    private static IntTreeNode tighten(IntTreeNode node) {
        if (node == null) {
            return null;
        } else if (node.left != null && node.right == null) {
            return tighten(node.left);
        } else if (node.left == null && node.right != null) {
            return tighten(node.right);
        } else {
            node.left = tighten(node.left);
            node.right = tighten(node.right);
            return node;
        }
    }
    public static void trim(IntTree tree, int min, int max) {
        tree.overallRoot=trim(tree.overallRoot, min, max);

    }
    private static IntTreeNode trim(IntTreeNode node, int min, int max) {
            if (node == null) {
                return null;
            }
            if (node.data> max)
            {
                return trim(node.left, min, max);
            }
            if (node.data < min)
            {
                return  trim(node.right, min, max);
            }
            node.left=trim(node.left, min, max);
            node.right=trim(node.right, min, max);
            return node;

    }
}
