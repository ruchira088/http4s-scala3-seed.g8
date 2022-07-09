#! /usr/bin/env node

const libSodium = require("libsodium-wrappers")
const yargs = require("yargs/yargs")
const { hideBin } = require("yargs/helpers")

const encrypt = async (publicKeyBytes, messageBytes) => {
    await libSodium.ready
    return libSodium.crypto_box_seal(messageBytes, publicKeyBytes)
}

const encryptValue = async (publicKey, value) => {
    const messageBytes = Buffer.from(value)
    const publicKeyBytes = Buffer.from(publicKey, "base64")
    const encryptedBytes = await encrypt(publicKeyBytes, messageBytes);
    const encryptedValue = Buffer.from(encryptedBytes).toString("base64")

    return encryptedValue
}

const {publicKey, secret} = yargs(hideBin(process.argv))
    .usage("Usage \$0 --public-key [public-key] --secret [secret]")
    .demandOption(["public-key", "secret"])
    .describe("public-key", "Public key of the GitHub repository")
    .alias("k", "public-key")
    .describe("secret", "Secret to be encrypted")
    .alias("s", "secret")
    .help("h")
    .alias("h", "help")
    .alias("v", "version")
    .argv

encryptValue(publicKey, secret)
    .then(encryptedValue => console.log(encryptedValue))