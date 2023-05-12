inline fun inlineTest() {
    test() // K2 reports: "Public-API inline function cannot access non-public-API '@PublishedApi() fun test(): Unit'"
}