package com.testography.amgradle.data.network.error;

public class NetworkAvailableError extends Throwable {
    public NetworkAvailableError() {
        super("The Internet is not accessable. Try again later");
    }
}
