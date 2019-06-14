package calculator.ast.operators;

import calculator.ast.AstNode;
import calculator.errors.EvaluationError;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
//import misc.exceptions.NotYetImplementedException;

public class ExpressionOperators {

    /**
     * Accepts an 'toDouble(inner)' AstNode and returns a new number node representing a
     * simplified version of the AstNode 'inner'.
     *
     * Preconditions:
     *
     * - The 'node' parameter is an operation AstNode with the name 'toDouble'.
     * - The 'node' parameter has exactly one child: the AstNode to convert into a double.
     *
     * Postconditions:
     *
     * - Returns a number AstNode containing the computed double.
     *
     * There are no other side effects for the inputs.
     *
     * For example, if this method receives the AstNode corresponding to
     * 'toDouble(3 + 4)', this method should return the AstNode corresponding
     * to '7'.
     * 
     * This method is required to handle the following binary operations:
     *      +, -, *, /, ^
     *  (addition, subtraction, multiplication, division, and exponentiation, respectively) 
     * and the following unary operations:
     *      negate, sin, cos
     *
     * @throws EvaluationError  if any of the expressions contains an undefined variable.
     * @throws EvaluationError  if any of the expressions uses an unknown operation.
     */
    public static AstNode handleToDouble(AstNode node, IDictionary<String, AstNode> variables) {
        // To help you get started, we've implemented this method for you.
        // You should fill in the locations specified by "your code here"
        // in the 'toDoubleHelper' method.
        //
        // If you're not sure why we have a public method calling a private
        // recursive helper method, review your notes from CSE 143 (or the
        // equivalent class you took) about the 'public-private pair' pattern.

        AstNode.assertOperatorValid("toDouble", 1, node);
        AstNode exprToConvert = node.getChildren().get(0);
        return new AstNode(toDoubleHelper(exprToConvert, variables));
    }

    // This method has default (package-private) access so that it can be used in handlePlot.
    static double toDoubleHelper(AstNode node, IDictionary<String, AstNode> variables) {
        // There are three types of nodes, so we have three cases.
        if (node.isNumber()) {
            // throw new NotYetImplementedException();
            return node.getNumericValue();
        } else if (node.isVariable()) {
            // throw new NotYetImplementedException();
            if (variables.containsKey(node.getName())) {
                return toDoubleHelper(variables.get(node.getName()), variables);
            } else {
                throw new EvaluationError("Attempted to convert an undefined variable");
            }
        } else {
            // You may assume the expression node has the correct number of children.
            // If you wish to make your code more robust, you can also use the provided
            // "assertNodeMatches" method to verify the input is valid.
            String name = node.getName();
            // your code
            if (name.equals("sin")) {
                AstNode.assertOperatorValid(name, 1, node);
                AstNode nextNode = node.getChildren().get(0);
                return Math.sin(toDoubleHelper(nextNode, variables));
            } else if (name.equals("cos")) {
                AstNode.assertOperatorValid(name, 1, node);
                AstNode nextNode = node.getChildren().get(0);
                return Math.cos(toDoubleHelper(nextNode, variables));
            } else if (name.equals("negate")) {
                AstNode.assertOperatorValid(name, 1, node);
                AstNode nextNode = node.getChildren().get(0);
                return -(toDoubleHelper(nextNode, variables));
            } else if (name.equals("+")) {
                AstNode.assertOperatorValid(name, 2, node);
                AstNode nextNode1 = node.getChildren().get(0);
                AstNode nextNode2 = node.getChildren().get(1);
                return toDoubleHelper(nextNode1, variables) + toDoubleHelper(nextNode2, variables);
            } else if (name.equals("-")) {
                AstNode.assertOperatorValid(name, 2, node);
                AstNode nextNode1 = node.getChildren().get(0);
                AstNode nextNode2 = node.getChildren().get(1);
                return toDoubleHelper(nextNode1, variables) - toDoubleHelper(nextNode2, variables);
            } else if (name.equals("*")) {
                AstNode.assertOperatorValid(name, 2, node);
                AstNode nextNode1 = node.getChildren().get(0);
                AstNode nextNode2 = node.getChildren().get(1);
                return toDoubleHelper(nextNode1, variables) * toDoubleHelper(nextNode2, variables);
            } else if (name.equals("/")) {
                AstNode.assertOperatorValid(name, 2, node);
                AstNode nextNode1 = node.getChildren().get(0);
                AstNode nextNode2 = node.getChildren().get(1);
                return toDoubleHelper(nextNode1, variables) / toDoubleHelper(nextNode2, variables);
            } else if (name.equals("^")) {
                AstNode.assertOperatorValid(name, 2, node);
                AstNode nextNode1 = node.getChildren().get(0);
                AstNode nextNode2 = node.getChildren().get(1);
                return Math.pow(toDoubleHelper(nextNode1, variables), toDoubleHelper(nextNode2, variables));
            } else {
                throw new EvaluationError("unknown operation");
            }
            // throw new NotYetImplementedException();
        }
    }

    /**
     * Accepts a 'simplify(inner)' AstNode and returns a new node containing a simplified version
     * of the 'inner' AstNode.
     *
     * Preconditions:
     *
     * - The 'node' parameter is an operation AstNode with the name 'simplify'.
     * - The 'node' parameter has exactly one child: the AstNode to simplify
     *
     * Postconditions:
     *
     * - Returns an AstNode containing the simplified inner parameter.
     *
     * There are no other side effects for the inputs.
     *
     * For example, if we received the AstNode corresponding to the expression
     * "simplify(3 + 4)", you would return the AstNode corresponding to the
     * number "7".
     *
     * Note: there are many possible simplifications we could implement here,
     * but you are only required to implement a single one: constant folding.
     *
     * That is, whenever you see expressions of the form "NUM + NUM", or
     * "NUM - NUM", or "NUM * NUM", simplify them.
     */
    public static AstNode handleSimplify(AstNode node, IDictionary<String, AstNode> variables) {
        // Try writing this one on your own!
        // Hint 1: Your code will likely be structured roughly similarly
        //         to your "handleToDouble" method
        // Hint 2: When you're implementing constant folding, you may want
        //         to call your "handleToDouble" method in some way
        // Hint 3: When implementing your private pair, think carefully about
        //         when you should recurse. Do you recurse after simplifying
        //         the current level? Or before?

        AstNode.assertOperatorValid("simplify", 1, node);
        AstNode exprToConvert = node.getChildren().get(0);
        return simplifyHelper(exprToConvert, variables);
        // throw new NotYetImplementedException();
    }

    private static AstNode simplifyHelper(AstNode node, IDictionary<String, AstNode> variables) {
        if (node.isNumber()) {
            return node;
        } else if (node.isVariable()) {
            if (variables.containsKey(node.getName())) {
                return variables.get(node.getName());
            } else {
                return node;
            }
        } else {
            String name = node.getName();
            if (name.equals("+") || name.equals("-") || name.equals("*")) {
                AstNode nextNode1 = node.getChildren().get(0);
                AstNode nextNode2 = node.getChildren().get(1);
                if (nextNode1.isNumber() && nextNode2.isNumber()) {
                    return new AstNode(toDoubleHelper(node, variables));
                }
                AstNode newNextNode1 = simplifyHelper(nextNode1, variables);
                AstNode newNextNode2 = simplifyHelper(nextNode2, variables);
                IList<AstNode> newChildren = new DoubleLinkedList<>();
                newChildren.add(newNextNode1);
                newChildren.add(newNextNode2);
                AstNode newNode = new AstNode(name, newChildren);
                if (newNextNode1.isNumber() && newNextNode2.isNumber()) {
                    return new AstNode(toDoubleHelper(newNode, variables));
                }
                return newNode;
            } else if (name.equals("sin") || name.equals("cos") || name.equals("negate")) {
                AstNode nextNode = node.getChildren().get(0);
                AstNode newNextNode = simplifyHelper(nextNode, variables);
                IList<AstNode> newChildren = new DoubleLinkedList<>();
                newChildren.add(newNextNode);
                AstNode newNode = new AstNode(name, newChildren);
                return newNode;
            } else {
                AstNode nextNode1 = node.getChildren().get(0);
                AstNode nextNode2 = node.getChildren().get(1);
                AstNode newNextNode1 = simplifyHelper(nextNode1, variables);
                AstNode newNextNode2 = simplifyHelper(nextNode2, variables);
                IList<AstNode> newChildren = new DoubleLinkedList<>();
                newChildren.add(newNextNode1);
                newChildren.add(newNextNode2);
                AstNode newNode = new AstNode(name, newChildren);
                return newNode;
            }
        }
        // throw new NotYetImplementedException();
    }
}
