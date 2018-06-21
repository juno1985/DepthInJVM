package my.hashmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyHashMap<K, V> implements MyMap<K, V> {  
	  
    // 定义默认数组的大小  2^n  16
    private static int defaultLength = 1<<4;  
      
    /** 
     * 1、哈希表内部的数组的长度，若使用的数组长度超过总长度的0.75 则数组需要进行扩容，扩容将就是将数组长度变为原来的两倍， 
     *      并且将现在的数组内的数据重新hash并均匀的散列分布到新的数组中，所以该操作消耗比较大 
     * 2、defaultDaaSizeFactory值是经过测试得出，考虑各种因素后的均衡值，值过大会照成扩容的概率下降，消耗少但是存取数据的效率也降低（反之。。。） 
     * 3、0.9或者更大的扩容值，会内置形成大量的链表，存取都需要进行很多的next（）判定查找操作 
     */  
    private static double defaultAddSizeFactory = 0.75;  
      
    /** 
     * 已使用数组的长度（put操作或判定并进行++操作） 
     */  
    private int userSize;  
      
    // 数组之一（即数组中的链表，初始化为null）  
    private Entry<K,V>[] table = null;  
      
    /** 
     * 无参的构造函数,使用默认值(与带参数的构造形成门面模式) 
     */  
    public MyHashMap(){  
        this(defaultLength, defaultAddSizeFactory);  
    }  
      
    /** 
     * 带两个参数的构造函数 
     * @param length    外置的数组默认大小 
     * @param defaultAddSizeFactory 外置的扩容值 
     */  
    public MyHashMap(int length, double defaultAddSizeFactory2){  
        if(length < 0){  
            throw new IllegalArgumentException("参数不能为负数:");  
        }  
        if(defaultAddSizeFactory <= 0 || Double.isNaN(defaultAddSizeFactory)){  
            throw new IllegalArgumentException("扩容参数必须大于0");  
        }  
        defaultLength = length;  
        defaultAddSizeFactory = defaultAddSizeFactory2;  
        table = new Entry[defaultLength];  
    }  
      
    class Entry<K,V> implements MyMap.Entry<K, V>{  
          
        K k;  
        V v;  
        // 指向下一个对象  
        Entry<K,V> next = null;  
          
        // 全参构造  
        public Entry(K k, V v, Entry<K, V> next) {  
            super();  
            this.k = k;  
            this.v = v;  
            this.next = next;  
        }  
  
        @Override  
        public K getKey() {  
            return k;  
        }  
  
        @Override  
        public V getValue() {  
            return v;  
        }  
          
    }  
    /** 
     * 存数据 
     * @param k 
     * @param v 
     */  
    @Override  
    public V put(K k, V v) {  
        // 判定是否需要进行扩容  
        if(userSize > defaultAddSizeFactory*defaultLength){  
            up2Size();  
        }  
        int index = getIndex(k, table.length);  
        Entry<K,V> entry = table[index];  
        // 判定index的数组下标位置中是否已经存放过数据，并且存放数据是后放的数据离的最近  
        if(entry == null){  
            table[index] = new Entry(k,v,null);
            userSize++;
        }else if(entry != null){  
            // 若已经被占用则需要构建一个新的链表  
            table[index] = new Entry(k,v,entry);   
        }  
        return table[index].getValue();  
    }  
  
    /** 
     * 寻找数组位置的下标 
     * @param k 
     * @param length 
     * @return 
     */  
    private int getIndex(K k, int length) {  
        int i = length - 1;  
        //index -> [0,length)
        int index = hash(k.hashCode())&i;  
        return index;  
    }  
  
    /** 
     * 自己的hash方法(进行大量的位移3后使数据更加散列) 
     * @param hashCode 
     * @return 
     */  
    private int hash(int hashCode) {  
        hashCode = hashCode^((hashCode>>>20)^(hashCode>>>12));  
        return hashCode^((hashCode>>>7)^(hashCode>>>4));  
    }  
  
    /** 
     * 数组进行两倍扩容 
     * 1、创建两倍长度的新数组 
     * 2、 
     */  
    private void up2Size() {  
        Entry<K,V>[] newTable = new Entry[defaultLength*2];  
        // 将原数组中的数组再次进行hash后放入新数组中  
        againHash(newTable);  
    }  
  
    /** 
     * 将原数组中的数组再次进行hash后放入新数组中 
     * @param newTable 
     */  
    private void againHash(MyHashMap<K, V>.Entry<K, V>[] newTable) {  
        Map<String,String> map = new HashMap<String,String>();  
        List<Entry<K,V>> entryList = new ArrayList<MyHashMap<K, V>.Entry<K,V>>();  
        // 循环遍历数组并将数据加载值entryList中  
        for(int i=0 ; i<table.length ; i++){  
            if(table[i] == null){  
                continue;  
            }  
            // 继续查找数组Entry对象  
            foundEntryByNext(table[i], entryList);  
        }  
    }  
  
    // 查找下一个元素并将数据存入list中  
    private void foundEntryByNext(MyHashMap<K, V>.Entry<K, V> entry,  
            List<MyHashMap<K, V>.Entry<K, V>> entryList) {  
        if(entry != null && entry.next != null){  
            entryList.add(entry);  
            //进行递归查找  
            foundEntryByNext(entry.next, entryList);  
        }else{  
            // 没有nest元素指向  
            entryList.add(entry);  
        }  
    }  
  
    /** 
     * 取数据 
     * @param k 
     */  
    @Override  
    public V get(K k) {  
        int index = getIndex(k,table.length);  
        if(table[index] == null){  
            throw new NullPointerException();  
        }  
        return findValueByEqualKey(k, table[index]);  
    }  
      
    private V findValueByEqualKey(K k, MyHashMap<K, V>.Entry<K, V> entry) {  
        if(k== entry.getKey() || k.equals(entry.getKey())){  
            return entry.getValue();  
        }else if(entry.next != null){  
            return findValueByEqualKey(k,entry.next);  
        }  
        return null;  
    }  
  
    // 获取当时使用了多少个数组  
    public int getUseSize(){  
        return userSize;  
    }  
  
    // 测试  
    public static void main(String[] args) {  
        MyMap<String,String> map = new  MyHashMap<String,String>();  
        for(int i=0 ; i<100 ; i++ ){  
            map.put(i+"", "测试"+i);  
        }  
        for(int i=0 ; i<100 ; i++ ){  
            System.out.println("第"+ i +"个对象值为： "+ map.get(i+"") );  
        }  
    }  
}  
