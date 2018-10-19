package com.ziyuan;

/**
 * BuddyAlloc
 *
 * @author xiuyuhang [xiuyuhang]
 * @since 2018-10-19
 */
public class BuddyAlloc {

    public static void main(String[] args) {
        BuddyAllocator b = new BuddyAllocator(4);

//        System.out.println(b.allocOffset(2));
        System.out.println(b.allocOffset(1));
        b.printNodes();

        b.free(0);

        b.printNodes();
    }
}

class BuddyAllocator {

    private int size;

    private int[] nodes;

    private int[] memory;

    BuddyAllocator(int size) {
        if (size < 1 || !isPowerOfTwo(size)) {
            throw new IllegalArgumentException("size should be power of 2");
        }
        this.size = size;
        int nodeSize = 2 * size;
        nodes = new int[nodeSize - 1];
        for (int i = 0; i < 2 * size - 1; i++) {
            if (isPowerOfTwo(i + 1)) {
                nodeSize /= 2;
            }
            nodes[i] = nodeSize;
        }

        memory = new int[size];
        for (int i = 0; i < memory.length; i++) {
            memory[i] = 0;
        }
    }

    public void alloc(int needSize) {
        int offset = allocOffset(needSize);
        int maxOffset = offset + needSize;
        for (int i = offset; i < maxOffset; i++) {
            memory[i] = 1;
        }
    }

    public void free(int offset) {
        if (offset < 0 || offset >= size) {
            throw new IllegalArgumentException("offset");
        }

        int nodeSize = 1;
        int index = offset + size - 1;

        // 找到offset父节点
        for (; nodes[index] > 0; index = parent(index)) {
            nodeSize *= 2;

            // 如果一直回溯到nodes[0]了，则直接返回。
            // 什么情况下会走到这里：
            // 1.在没有分配就直接free的情况下
            // 2.分配的是offset 1的情况下，free offset 2
            if (index == 0) {
                return;
            }
        }

        // 设置为正确的size
        // 如果分配最小单位，则就把当前单元设置为1
        // 如果不是，这里的index实际上是父节点，把父节点的值设置为正确的值，比如2
        nodes[index] = nodeSize;

        // 合并，将父节点恢复满状态的值
        int leftNodes, rightNodes;
        while (index > 0) {
            index = parent(index);
            nodeSize *= 2;

            leftNodes = nodes[leftLeaf(index)];
            rightNodes = nodes[rightLeaf(index)];

            if (leftNodes + rightNodes == nodeSize) {
                nodes[index] = nodeSize;
            } else {
                nodes[index] = Math.max(leftNodes, rightNodes);
            }
        }
    }

    public int allocOffset(int needSize) {
        if (needSize <= 0) {
            needSize = 1;
        } else if (!isPowerOfTwo(needSize)) {
            needSize = fixSize(needSize);
        }

        int index = 0;
        int offset;
        int nodeSize;

        if (nodes[index] < needSize) {
            return -1;
        }

        // 找到合适的大小的node的index
        for (nodeSize = this.size; nodeSize != needSize; nodeSize /= 2) {
            if (nodes[leftLeaf(index)] >= needSize) {
                index = leftLeaf(index);
            } else {
                index = rightLeaf(index);
            }
        }

        // 把找到的节点标记为0 表示这个节点都已经被占用了
        nodes[index] = 0;

        // 把index回归到memory的offset上
        offset = (index + 1) * nodeSize - this.size;

        // 回溯父节点，把父节点标记为左右叶子节点中，值大的那个
        while (index > 0) {
            index = parent(index);
            nodes[index] = Math.max(nodes[leftLeaf(index)], nodes[rightLeaf(index)]);
        }

        return offset;
    }

    public void printNodes() {
        System.out.println("nodes:");
        for (int node : nodes) {
            System.out.println(node);
        }
//        System.out.println("memory:");
//        for (int m : memory) {
//            System.out.println(m);
//        }
    }

    private static boolean isPowerOfTwo(int x) {
        int y = x - 1;
        int r = x & y;
        return r == 0;
    }

    private int leftLeaf(int index) {
        return index * 2 + 1;
    }

    private int rightLeaf(int index) {
        return index * 2 + 2;
    }

    private int parent(int index) {
        return (index + 1) / 2 - 1;
    }

    private static int fixSize(int size) {
        size |= size >> 1;
        size |= size >> 2;
        size |= size >> 4;
        size |= size >> 8;
        size |= size >> 16;
        return size + 1;
    }
}
