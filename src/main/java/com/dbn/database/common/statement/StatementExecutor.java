package com.dbn.database.common.statement;

import com.dbn.common.thread.Threads;
import com.dbn.common.thread.Timeout;
import com.dbn.connection.Resources;
import lombok.experimental.UtilityClass;

import java.sql.SQLException;
import java.util.concurrent.*;

import static com.dbn.common.exception.Exceptions.*;
import static com.dbn.diagnostics.Diagnostics.conditionallyLog;

@UtilityClass
public final class StatementExecutor {

    public static <T> T execute(StatementExecutorContext context, Callable<T> callable) throws SQLException {
        long start = System.currentTimeMillis();
        int timeout = context.getTimeout();
        try {
            ExecutorService executorService = Threads.databaseInterfaceExecutor();
            Future<T> future = executorService.submit(callable);
            T result = Timeout.waitFor(future, timeout, TimeUnit.SECONDS);

            context.log("QUERY", false, false, millisSince(start));
            return result;

        } catch (TimeoutException | InterruptedException | RejectedExecutionException e) {
            conditionallyLog(e);
            context.log("QUERY", false, true, millisSince(start));
            Resources.close(context.getStatement());
            throw toSqlTimeoutException(e, "Operation timed out (timeout = " + timeout + "s)");

        } catch (ExecutionException e) {
            conditionallyLog(e);
            context.log("QUERY", true, false, millisSince(start));
            Resources.close(context.getStatement());
            Throwable cause = causeOf(e);
            throw toSqlException(cause, "Error processing request: " + cause.getMessage());

        } catch (Throwable e) {
            conditionallyLog(e);
            context.log("QUERY", true, false, millisSince(start));
            Resources.close(context.getStatement());
            throw toSqlException(e, "Error processing request: " + e.getMessage());

        }
    }

    private static long millisSince(long start) {
        return System.currentTimeMillis() - start;
    }
}
