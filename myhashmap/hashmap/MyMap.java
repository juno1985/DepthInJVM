package my.hashmap;

public interface MyMap<K, V> {  
	  
    V put(K k, V v);  
      
    V get(K k);  
      
    // hashMap�ڲ���Entry����  
    interface Entry<K, V>{  
          
        public K getKey();  
          
        public V getValue();  
    }  
}  