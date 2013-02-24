
package components.graph.features;

public interface FeatureInterface {

    <T> Object getInterface();

    <T> Object up(); // shorter alias for getInterface()
}