package pen

class KSodium : Sodium
{
   override fun sodium_init () = 0
   override fun randombytes_buf (buf : ByteArray, size : Long) {}

   /* HMAC */
   override fun crypto_generichash (output : ByteArray, outlen : Long, input : ByteArray, inlen : Long, key : ByteArray?, keylen : Long) = 0

   /* Keying */
   override fun crypto_sign_ed25519_seed_keypair (pk : ByteArray, sk : ByteArray, seed : ByteArray) = 0
   override fun crypto_sign_ed25519_sk_to_curve25519 (curve25519_sk : ByteArray, ed25519_sk : ByteArray) = 0
   override fun crypto_sign_ed25519_pk_to_curve25519 (curve25519_pk : ByteArray, ed25519_pk : ByteArray) = 0
   override fun crypto_pwhash (key : ByteArray, keylen : Long, passwd : ByteArray, passwdlen: Long, salt : ByteArray, opslimit : Long, memlimit : Long, alg : Int) = 0
   override fun crypto_pwhash_alg_default () = 0
   override fun crypto_pwhash_saltbytes () = 0L
   override fun crypto_pwhash_opslimit_moderate () = 0L
   override fun crypto_pwhash_memlimit_moderate () = 0L

   /* Signing */
   override fun crypto_sign_ed25519 (sm : ByteArray, smlen_p: Long, m : ByteArray, mlen : Long, sk : ByteArray) = 0
   override fun crypto_sign_ed25519_open (m : ByteArray, mlen_p : Long, sm : ByteArray, smlen : Long, pk : ByteArray) = 0
   override fun crypto_sign_ed25519_detached (sig : ByteArray, siglen_p : Long, m : ByteArray, mlen : Long, sk : ByteArray) = 0
   override fun crypto_sign_ed25519_verify_detached (sig : ByteArray, m : ByteArray, mlen : Long, pk : ByteArray) = 0
   override fun crypto_sign_ed25519_seedbytes () = 0L
   override fun crypto_sign_ed25519_secretkeybytes () = 0L
   override fun crypto_sign_ed25519_publickeybytes () = 0L
   override fun crypto_sign_ed25519_bytes() = 0L

   /* Public key encryption */
   override fun crypto_box_easy (c : ByteArray, m : ByteArray, mlen : Long, n : ByteArray, pk : ByteArray, sk : ByteArray) = 0
   override fun crypto_box_open_easy (m : ByteArray, c : ByteArray, clen : Long, n : ByteArray, pk : ByteArray, sk : ByteArray) = 0
   override fun crypto_box_publickeybytes () = 0L
   override fun crypto_box_secretkeybytes () = 0L
   override fun crypto_box_noncebytes () = 0L
   override fun crypto_box_macbytes () = 0L

   /* Secret key encryption */
   override fun crypto_secretbox_easy (c : ByteArray, m : ByteArray, mlen : Long, n : ByteArray, k : ByteArray) = 0
   override fun crypto_secretbox_open_easy (m : ByteArray, c : ByteArray, clen : Long, n : ByteArray, k : ByteArray) = 0
   override fun crypto_secretbox_keybytes () = 0L
   override fun crypto_secretbox_noncebytes () = 0L
   override fun crypto_secretbox_macbytes () = 0L
}
