package de.coerdevelopment.standalone.net.tcp;

import de.coerdevelopment.standalone.net.Datapackage;

public abstract class TcpMethod {

    protected final String methodID;

    public TcpMethod(String methodID) {
        this.methodID = methodID;
    }

    public abstract void onMethod(Datapackage incomingPackage, TcpThread clientThread);

    public final String getMethodID() {
        return methodID;
    }

}
