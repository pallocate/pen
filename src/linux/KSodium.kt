package pen

import pen.PasswordProvider

class KSodium : Sodium
{
   override fun sodium_init () = sodium.sodium_init()                           // core.h
   override fun randombytes_buf (buf : ByteArray, size : Int) {}                // randombytes_buf.h

   /* HMAC. crypto_generichash.h */
   override fun crypto_generichash (output : ByteArray, outlen : Int, input : ByteArray, inlen : Int, key : ByteArray?, keylen : Int) = 0

   /* Keying. crypto_sign_ed25519.h. crypto_pwhash.h */
   override fun crypto_sign_ed25519_seed_keypair (pk : ByteArray, sk : ByteArray, seed : ByteArray) = 0
   override fun crypto_sign_ed25519_sk_to_curve25519 (curve25519_sk : ByteArray, ed25519_sk : ByteArray) = 0
   override fun crypto_sign_ed25519_pk_to_curve25519 (curve25519_pk : ByteArray, ed25519_pk : ByteArray) = 0
   override fun crypto_pwhash (key : ByteArray, keylen : Long, passwd : ByteArray, passwdlen: Long, salt : ByteArray, opslimit : Long, memlimit : Long, alg : Int) = 0
   override fun crypto_pwhash_alg_default () = sodium.crypto_pwhash_ALG_DEFAULT
   override fun crypto_pwhash_saltbytes () = sodium.crypto_pwhash_SALTBYTES.toLong()
   override fun crypto_pwhash_opslimit_moderate () = sodium.crypto_pwhash_MEMLIMIT_MODERATE.toLong()
   override fun crypto_pwhash_memlimit_moderate () = sodium.crypto_pwhash_MEMLIMIT_MODERATE.toLong()

   /* Signing. crypto_sign_ed25519.h */
   override fun crypto_sign_ed25519 (sm : ByteArray, smlen_p: Long, m : ByteArray, mlen : Long, sk : ByteArray) = 0
   override fun crypto_sign_ed25519_open (m : ByteArray, mlen_p : Long, sm : ByteArray, smlen : Long, pk : ByteArray) = 0
   override fun crypto_sign_ed25519_detached (sig : ByteArray, siglen : Long, m : ByteArray, mlen : Long, sk : ByteArray) = 0
   override fun crypto_sign_ed25519_verify_detached (sig : ByteArray, m : ByteArray, mlen : Long, pk : ByteArray) = 0
   override fun crypto_sign_ed25519_seedbytes () = sodium.crypto_sign_ed25519_SEEDBYTES.toLong()
   override fun crypto_sign_ed25519_secretkeybytes () = sodium.crypto_sign_ed25519_SECRETKEYBYTES.toLong()
   override fun crypto_sign_ed25519_publickeybytes () = sodium.crypto_sign_ed25519_PUBLICKEYBYTES.toLong()
   override fun crypto_sign_ed25519_bytes() = sodium.crypto_sign_ed25519_BYTES.toLong()

   /* Public key encryption. crypto_box.h */
   override fun crypto_box_easy (c : ByteArray, m : ByteArray, mlen : Long, n : ByteArray, pk : ByteArray, sk : ByteArray) = 0
   override fun crypto_box_open_easy (m : ByteArray, c : ByteArray, clen : Long, n : ByteArray, pk : ByteArray, sk : ByteArray) = 0
   override fun crypto_box_publickeybytes () = sodium.crypto_box_PUBLICKEYBYTES.toLong()
   override fun crypto_box_secretkeybytes () = sodium.crypto_box_SECRETKEYBYTES.toLong()
   override fun crypto_box_noncebytes () = sodium.crypto_box_NONCEBYTES.toLong()
   override fun crypto_box_macbytes () = sodium.crypto_box_MACBYTES.toLong()

   /* Secret key encryption. crypto_secretbox.h */
   override fun crypto_secretbox_easy (c : ByteArray, m : ByteArray, mlen : Long, n : ByteArray, k : ByteArray) = 0
   override fun crypto_secretbox_open_easy (m : ByteArray, c : ByteArray, clen : Long, n : ByteArray, k : ByteArray) = 0
   override fun crypto_secretbox_keybytes () = sodium.crypto_secretbox_KEYBYTES.toLong()
   override fun crypto_secretbox_noncebytes () = sodium.crypto_secretbox_NONCEBYTES.toLong()
   override fun crypto_secretbox_macbytes () = sodium.crypto_secretbox_MACBYTES.toLong()
}

