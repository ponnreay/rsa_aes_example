# AES + RSA Encryption

### Note: how to generate key pair:
```bash
cd keys

# generate [2048-bit RSA private key]
openssl genrsa -out private_key.pem 2048

# get public key from private key above
openssl rsa -in private_key.pem -pubout -out public_key.crt

# convert [2048-bit RSA private key] to PKCS#8 format (so java can read it)
openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in private_key.pem -out private_key.key
```
After run above command, we generate 3 file: `private_key.pem`, `private_key.key` and `public_key.crt` <br/>
But in this demo, we only use `private_key.key` and `public_key.crt`


