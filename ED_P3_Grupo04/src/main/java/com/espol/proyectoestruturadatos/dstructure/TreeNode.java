package com.espol.proyectoestruturadatos.dstructure;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Gabriel Cevallos, Dylan Jeanpier Pincay Salazar, Helen Cruz
 */
public class TreeNode<E> {
    private E data;
    private TreeNode<E> parent;
    private List<TreeNode<E>> children;
    private int utility;
    private int movement;
    private int depth;
    private boolean maximizing;

    public TreeNode(E data, int movement, int depth, boolean maximizing) {
        this.data = data;
        this.parent = null;
        this.children = new ArrayList<>();
        this.utility = 0;
        this.movement = movement;
        this.depth = depth;
        this.maximizing = maximizing;
    }

    public E getData() {
        return data;
    }

    public List<TreeNode<E>> getChildren() {
        return children;
    }

    public void addChild(TreeNode<E> child) {
        child.parent = this;
        this.children.add(child);
    }

    public boolean isLeaf() {
        return children.isEmpty();
    }

    public int getUtility() {
        return utility;
    }

    public void setUtility(int utility) {
        this.utility = utility;
    }

    public int getMovement() {
        return movement;
    }

    public int getDepth() {
        return depth;
    }

    public boolean isMaximizing() {
        return maximizing;
    }
}
