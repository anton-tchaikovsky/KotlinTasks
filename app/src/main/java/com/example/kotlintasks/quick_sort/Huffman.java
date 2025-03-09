package com.example.kotlintasks.quick_sort;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class Huffman {

    String text;
    Map<Character, Integer> freqMap = new HashMap<>();
    Map<Character, String> encodeMap = new HashMap<>();
    Node root;
    StringBuilder sb = new StringBuilder();

    public Huffman(String text){
        this.text = text;
        createFreqMap(text);
        createHuffmanTree();
    }

    public String getEncodedText() {
        encode(root, "", encodeMap);

        System.out.println("Частотная таблица:");
        for (Map.Entry<Character, String> entry : encodeMap.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }

        for (int i = 0 ; i < text.length(); i++) {
            sb.append(encodeMap.get(text.charAt(i)));
        }

        return sb.toString();
    }

    public void printDecodedText() {
        int index = -1;
        while (index < sb.length() - 2) {
            index = decode(root, index, sb);
        }
    }

    private static void encode(Node root, String str, Map<Character, String> huffmanCode) {
        if (root == null)
            return;

        if (root.left == null && root.right == null) {
            huffmanCode.put(root.ch, str);
        }

        encode(root.left, str + "0", huffmanCode);
        encode(root.right, str + "1", huffmanCode);
    }

    private static int decode(Node root, int index, StringBuilder sb) {
        if (root == null)
            return index;

        if (root.left == null && root.right == null) {
            System.out.print(root.ch);
            return index;
        }

        index++;

        if (sb.charAt(index) == '0')
            index = decode(root.left, index, sb);
        else
            index = decode(root.right, index, sb);

        return index;
    }

    private void createFreqMap(String text){
        for (int i = 0 ; i < text.length(); i++) {
            if (!freqMap.containsKey(text.charAt(i))) {
                freqMap.put(text.charAt(i), 1);
            } else {
                //noinspection DataFlowIssue
                freqMap.put(text.charAt(i), freqMap.get(text.charAt(i)) + 1);
            }
        }
    }

    private void createHuffmanTree(){
        // Создание приоритетной очереди
        PriorityQueue<Node> nodePriorityQueue = new PriorityQueue<>(Comparator.comparingInt(l -> l.freq));

        // Заполнение приоритетной очереди корневыми узлами
        for (Map.Entry<Character, Integer> entry : freqMap.entrySet()) {
            nodePriorityQueue.add(new Node(entry.getKey(), entry.getValue()));
        }

        // Строим дерево Хаффмана
        while (nodePriorityQueue.size() != 1)
        {
            // Берем из очереди два узла с наименьшей частотой
            Node left = nodePriorityQueue.poll();
            Node right = nodePriorityQueue.poll();

            // Создаем и добавляем в очередь новый корневой узел с суммарной частотой и с ссылками
            // на два текущих узла
            assert left != null;
            assert right != null;
            int sum = left.freq + right.freq;
            nodePriorityQueue.add(new Node('\0', sum, left, right));
        }

        root = nodePriorityQueue.peek();
    }

    public static class Node {
        char ch;
        int freq;
        Node left = null, right = null;

        public Node(char ch, int freq) {
            this.ch = ch;
            this.freq = freq;
        }

        public Node(char ch, int freq, Node left, Node right) {
            this(ch, freq);
            this.left = left;
            this.right = right;
        }
    }
}
