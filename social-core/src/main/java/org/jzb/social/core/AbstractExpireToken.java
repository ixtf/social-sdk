package org.jzb.social.core;

/**
 * 描述：
 *
 * @author jzb 2017-10-25
 */
public abstract class AbstractExpireToken<T> {
    private T t;
    private long createInMillis;
    private long expiresInMillis;

    public T get() throws Exception {
        if (t == null || System.currentTimeMillis() > expiresInMillis) {
            _fetch();
        }
        return t;
    }

    synchronized private final void _fetch() throws Exception {
        if (t != null && System.currentTimeMillis() < expiresInMillis) {
            return;
        }
        createInMillis = System.currentTimeMillis();
        fetch();
    }

    abstract protected void fetch() throws Exception;

    protected void setT(T t) {
        this.t = t;
    }

    protected void setExpiresInMillis(long expiresInMillis) {
        this.expiresInMillis = expiresInMillis;
    }

    protected long getCreateInMillis() {
        return createInMillis;
    }
}
