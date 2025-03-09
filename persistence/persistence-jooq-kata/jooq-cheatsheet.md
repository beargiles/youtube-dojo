# jOOQ Cheatsheet

## Parameter binding

Parameter binding is handled by creating a `Param<T>` object that binds a property name and a
class and a `bind(String, T)` call that provides the value to use when executing the query.

Example

```java
import com.coyotesong.dojo.youtube.model.Channel;

import static com.coyotesong.dojo.youtube.repository.jooq.generated.tables.CHANNEL;

public class ChannelRepositoryJooq implements ChannelRepository {

    private static final ResultQuery<? extends ChannelRecord> CHANNEL_FIND_BY_ID_QUERY;

    static {
        CHANNEL_FIND_BY_ID_QUERY = (ResultQuery<? extends ChannelRecord>) query =
                ctx().selectFrom(CHANNEL)
                        .where(param("id", String.class).equal(CHANNEL.ID));
    }

    @Nullable
    public Channel findByChannelId(@NotNull String id) {
        final Param<String> idParam = dsl.param("id", String.class);
        
        final @NotNull ResultQuery<? extends ChannelRecord> query = ctx()
                .selectFrom(CHANNEL)
                .where(idParam.eq(idField));
        
        // get list of parameters associated with query
        List<Param, Object> query.params(); -------------------------------------------- VERIFY THIS

        query.bind("id", id);
        return query.fetchSingleInto(Channel.class);
    }
}    
```    

## Prepared statements

Prepared statements can provide a significant performance improvement because the cost of optimizing
complex queries can be amortarized across many queries. With jOOQ we

```java

public class ChannelRepositoryJooq implements ChannelRepository {
    
    private static final ResultQuery<? extends ChannelRecord> CHANNEL_FIND_BY_ID_QUERY;
    private static final CloseableResultQuery<? extends ChannelRecord> CLOSEABLE_CHANNEL_FIND_BY_ID_QUERY
        = (CloseableResultQuery<? extends ChannelRecord> CHANNEL_FIND_BY_ID_QUERY;

    static {
        CHANNEL_FIND_BY_ID_QUERY = (ResultQuery<? extends ChannelRecord>) query =
                ctx().selectFrom(CHANNEL)
                        .where(param("id", String.class).equal(CHANNEL.ID));

        CLOSEABLE_CHANNEL_FIND_BY_ID_QUERY = (CloseableResultQuery<? extends ChannelRecord>)
                (CloseableResultQuery<? extends ChannelRecord>) query;
        CLOSEABLE_CHANNEL_FIND_BY_ID_QUER.keepStatement(true);
    }
    
    public Channel findByChannelId(@NotNull id) {
        try (CloseableResultQuery<? extends ChannelRecord> query = CLOSEABLE_CHANNEL_FIND_BY_ID_QUERY) {
            query.bind("channelId", channelId);
            return channel;
        }
    }
}

```