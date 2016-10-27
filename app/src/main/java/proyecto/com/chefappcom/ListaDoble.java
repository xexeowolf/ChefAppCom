package proyecto.com.chefappcom;

/**
 * Created by alfredo on 23/10/16.
 */

/**
 * Clase generica que implementa una lista doblemente enlazada.
 * @author Luis ALfredo Piedra Esquivel
 * @since 06/10/2016
 * @param <E> parametro que representa un tipo de variable generica que puede ser sustituida por cual otro tipo definido previamente.
 */
public class ListaDoble<E,T> {

    public NodoDoble<E,T> head,tail;
    public int size;

    /**
     * Constructor de la clase.
     */

    public ListaDoble(){
        head=tail=null;
        size=0;
    }
    /**Metodo que agrega un elemento al inicio de la lista.
     * @param llave datos que se almacenara en los nodos. Puede ser de cualquier tipo basico o previamente definido.
     */
    public void addFirst(E llave,T valor){
        NodoDoble<E,T> nuevo=new NodoDoble<E,T>(llave,valor);
        if(head==null){
            head=tail=nuevo;
        }else{
            nuevo.next=head;
            head.prev=nuevo;
            head=nuevo;
        }
        size++;
    }
    /**Metodo que agrega un elemento al final de la lista.
     * @param llave datos que se almacenara en los nodos. Puede ser de cualquier tipo basico o previamente definido.
     */
    public void addLast(E llave,T valor){
        NodoDoble<E,T> nuevo=new NodoDoble<E,T>(llave, valor);
        if(head==null){
            head=tail=nuevo;
        }else{
            tail.next=nuevo;
            nuevo.prev=tail;
            tail=nuevo;
        }
        size++;
    }
    /**Metodo que agrega un elemento en una posicion especifica de la lista.
     * @param index posicion donde se colocara el nuevo elemento en la lista.
     * @param llave datos que se almacenara en los nodos. Puede ser de cualquier tipo basico o previamente definido.
     */
    public void addAt(int index,E llave,T valor){
        NodoDoble<E,T> nuevo=new NodoDoble<E,T>(llave,valor);
        index=Math.abs(index);
        if(index==0){
            addFirst(llave,valor);
        }else if(index>=size){
            addLast(llave,valor);
        }else{
            NodoDoble<E,T> tmp=head;
            while(index!=0){
                tmp=tmp.next;
                index--;
            }
            nuevo.next=tmp;
            nuevo.prev=tmp.prev;
            tmp.prev.next=nuevo;
            tmp.prev=nuevo;
        }
        size++;
    }
    /**
     * Metodo que elimina el primer nodo de la lista.
     */
    public void deleteFirst(){
        if(head!=null){
            head=head.next;
            head.prev=null;
            size--;
        }
    }
    /**
     * Metodo que elimina el ultimo nodo de la lista.
     */
    public void deleteLast(){
        if(head!=null && tail!=null){
            tail=tail.prev;
            tail.next=null;
            size--;
        }
    }
    /**Metodo que elimina un elemento en una posicion especifica de la lista.
     * @param index posicion donde se encuentra el elemento que se desea eliminar.
     */
    public void deleteAt(int index){
        index=Math.abs(index);
        if(index==0){
            deleteFirst();
        }else if(index==size-1){
            deleteLast();
        }else if(index<size){
            NodoDoble<E,T> temp=head;
            while(index!=0){
                temp=temp.next;
                index--;
            }
            temp.prev.next=temp.next;
            temp.next.prev=temp.prev;
            temp=temp.next;
            size--;
        }else{
            System.out.print("Indice no valido\n");
        }
    }

    public void deleteAt(NodoDoble<E,T> puntero){
        if(puntero!=null && puntero.prev!=null && puntero.next!=null){
            puntero.prev.next=puntero.next;
            puntero.next.prev=puntero.prev;
        }
    }
    /**Metodo que busca un elemento en la lista y retorna true si lo encuentra, de lo contrario devuelve false.
     * @param elemento informacion que se busca dentro de la lista.Puede ser de cualquier tipo basico o previamente definido.
     * @return true o false
     */
    public boolean search(E elemento){
        NodoDoble<E,T> tmp=head;
        while(tmp!=null){
            if(tmp.llave==elemento){
                return true;
            }
            tmp=tmp.next;
        }
        return false;

    }
    /**Metodo que imprime en consola la informacion contenida en los nodos de la lista.
     */
    public void imprimir(){
        NodoDoble<E,T> tmp=head;
        while(tmp!=null){
            System.out.println("Llave: "+tmp.llave+" "+"valor asociado: "+tmp.valor);
            tmp=tmp.next;
        }
        System.out.print("\n");
    }
}
