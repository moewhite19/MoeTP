package cn.whiteg.moetp.api;

public abstract class CallBack {
    abstract public void onBreak();

    abstract public void onTeleport(boolean result);

    abstract public void onCancel();

    public abstract void tick();
}
