
package transport.protocol;

import java.lang.reflect.InvocationTargetException;

import transport.IOinterface;

import com.google.gson.JsonObject;

public class MessageHandler implements MessageHandlerInterface {

    private final String configuration;

    public String getConfiguration() {
        return this.configuration;
    }

    public MessageHandler() {
        this.configuration = null;
    }

    //TODO
    public MessageHandler(final String configuration) {
        this.configuration = configuration;
    }

    @Override
    public Object handle(final IOinterface io, final MessageInterface message) {

        Object object = null;

        final String type = message.getType();
        final JsonObject data = message.getData();

        try {
            //final String inflection = "transport.protocol." + ("" + type.charAt(0)).toUpperCase() + type.substring(1);
            final String inflection = "transport.events." + type + "$Builder";
            //System.out.println(inflection);
            //event = (EventInterface) Class.forName(inflection).getConstructor(IOinterface.class, JsonObject.class).newInstance(io, data);
            object = Class.forName(inflection).getMethod("build", IOinterface.class, JsonObject.class).invoke(null, io, data);
        }
        catch (final ClassNotFoundException e) {
            e.printStackTrace();
        }
        catch (final IllegalArgumentException e) {
            e.printStackTrace();
        }
        catch (final SecurityException e) {
            e.printStackTrace();
        }
        catch (final IllegalAccessException e) {
            e.printStackTrace();
        }
        catch (final InvocationTargetException e) {
            e.printStackTrace();
        }
        catch (final NoSuchMethodException e) {
            e.printStackTrace();
        }

        // TODO check for null and create null events
        return object;
    }

}