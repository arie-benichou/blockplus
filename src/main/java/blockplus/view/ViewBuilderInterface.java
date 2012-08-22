
package blockplus.view;

public interface ViewBuilderInterface {

    Object getInputSymbol();

    Object getOutputSymbol();

    Object show(final Object object);

    // TODO ?! map<?,?>
    Object out(Object symbol);

    Object up();

}