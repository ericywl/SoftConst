package FuzzingCalc;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {
    private List<TreeNode> children;
    private Grammar grammarType;

    public TreeNode(Grammar grammarType) {
        children = new ArrayList<>();
        this.grammarType = grammarType;
    }
}
