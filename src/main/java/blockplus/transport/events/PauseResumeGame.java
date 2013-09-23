
package blockplus.transport.events;

import blockplus.transport.IEndPoint;
import blockplus.transport.events.interfaces.IPauseResumeGame;

import com.google.common.base.Objects;
import com.google.gson.JsonObject;

public class PauseResumeGame implements IPauseResumeGame {

    private final IEndPoint endPoint;
    private final boolean isPaused;

    public static class Builder {

        public static PauseResumeGame build(final IEndPoint io, final JsonObject data) {
            return new PauseResumeGame(io, data.get("isPaused").getAsBoolean());
        }

    }

    public PauseResumeGame(final IEndPoint endPoint, final boolean isPaused) {
        this.endPoint = endPoint;
        this.isPaused = isPaused;
    }

    @Override
    public IEndPoint getEndpoint() {
        return this.endPoint;
    }

    @Override
    public boolean isPaused() {
        return this.isPaused;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("endpoint", this.getEndpoint())
                .add("isPaused", this.isPaused())
                .toString();
    }

}