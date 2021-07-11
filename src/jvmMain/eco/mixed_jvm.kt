package pen.eco

import kotlinx.serialization.json.Json

/** Deserializes a product quatities object from json. */
fun deserializePQ (jsonString : String) = Json.decodeFromString<KProductQuantities>( KProductQuantities.serializer(), jsonString )
/** Serializes a product quatities object to json. */
fun serializePQ (pq : KProductQuantities) = Json.encodeToString( KProductQuantities.serializer(), pq )
