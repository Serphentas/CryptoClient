### To do:
* Synced folder (synced remote files)
    * Content to be synced across all devices, whereas items on the DefaultFrame are managed individually
* File/directory sharing via a unique access link, possibly protected by a password
    * ACLs
* Drag and drop support for both DL and UL (from/to the client)
* UI adjustments
    * Options menu
    * Populate Preferences menu
* Benchmarking tool
* Logging (for crashes, etc.)
* API for developers

### Latest commit:
* Authentication over TLS and use scrypt for the password (or another decent KDF)
    * Three rounds:
        * The client sends its credentials to the authentication server. If correct and if an active subscription exists, the server replies with two tokens.
        * The client sends back the two tokens, one for the control and one for the I/Oserver. If valid, the client sends an scrypt digest of its encryption password (!= login password) to the I/O server.
            * This is just to check that the user does not enter a wrong encryption password, which would render file I/O impossible.
        * _to do_ The client reads the reply from the I/O server. If positive, the connections to the control and I/O server are kept open and the service may now be used.
* Separate control and I/O channels
    * Allows to upload/download files and perform other tasks at the same time
    * Control
        * Basic functions (lsfile, lsdir, cd, cwd, mkdir, rename, rm, exists)
        * Sharing (create and revoke links)
    * I/O
        * Upload and download files
* Single view on the DefaultFrame (unsynced remote files)
    * Basic functions
        * Upload -> entry under File menu (ctrl+o)
        * Download -> entry under File menu (ctrl+s) or popup menu via file table
        * Delete -> entry under File menu (del) or popup menu via file table
        * Create folder -> entry under File menu (ctrl+n)
        * Move -> popup menu via file table
        * Rename -> popum menu via file table
    * Set a download folder from the Preferences menu, or get asked for one each time
* On-the-fly encryption/decryption
    * Local files can be sent and retrieved to/from the I/O server
    * _to do_ The password that is used to derive each file's secret key is NOT the login password. Instead, a second password must be provided to perform encryption and decryption.

### Releases:
**v2.1.0-beta**
* Basic functions and file I/O from/to the an FTP server
* Logging

**v2.0.0-beta**
* Basic functions and file I/O from/to the an FTP server

**v1.0.0-beta:**
* Encryption/decryption of local files

### Deprecated features:
* FTP
    * FTP has been replaced with a custom protocol
* _To be implemented later on_ Parallelized mode available ([causes a memory leak](https://github.com/Serphentas/CryptoClient/issues/1))
    * Each select file will be process by its own Thread, effectively processing all files at the same time