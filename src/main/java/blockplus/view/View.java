
package blockplus.view;

import com.google.common.base.Preconditions;

public final class View<T extends ViewInterface> implements ViewBuilderInterface {

    public static <T extends ViewInterface> View<T> as(final Class<T> viewClass) {
        return new View<T>(viewClass);
    }

    private final Class<T> viewClass;

    public Class<T> getViewClass() {
        return this.viewClass;
    }

    private View(final Class<T> viewClass) {
        this.viewClass = viewClass;
    }

    /*
    @SuppressWarnings("unchecked")
    public ViewBuilder() {
        this.viewClass = (Class<? extends ViewInterface>) super.getClass().getEnclosingClass();
    }
    */

    private Object inputSymbol = null;

    @Override
    public Object getInputSymbol() {
        return this.inputSymbol;
    }

    private Object outputSymbol;

    @Override
    public Object getOutputSymbol() {
        return this.outputSymbol;
    }

    @Override
    public View<T> show(final Object inputSymbol) {
        this.inputSymbol = inputSymbol;
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T out(final Object outputSymbol) {
        this.outputSymbol = outputSymbol;
        ViewInterface viewInstance = null;
        try {
            viewInstance = this.getViewClass().getConstructor(this.getClass().getInterfaces()[0]).newInstance(this);
        }
        catch (final Exception e) {
            Preconditions.checkState(viewInstance != null, "Out failure on " + this.getViewClass() + ": " + e);
        }
        return (T) viewInstance.render();
    }

    @Override
    public T up() {
        return this.out(null);
    }

}