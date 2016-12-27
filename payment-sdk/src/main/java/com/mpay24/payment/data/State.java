package com.mpay24.payment.data;

public enum State {
	INIT,
    AUTHORIZE,
    SUSPENDED,
    REDIRECTED,
    CALLBACK,
    RESERVED,
    EXECUTE,
    BILLED,
    REVOKE,
    CREDITED,
    REVERSED,
    REJECT,
    ARCHIVED,
    WITHDRAW,
    WITHDRAWN,
	
    NOTFOUND,
    FAILED,

}
