
package blockplus.view;

public interface ViewInterface {

    //Map<?, Character> getSymbols();

    /*
    @Override
    String toString();
    */

    ViewInterface render();

    Object apply(Object object);

}