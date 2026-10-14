# NekoBox for Android

[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=21)
[![Releases](https://img.shields.io/github/v/release/MatsuriDayo/NekoBoxForAndroid)](https://github.com/MatsuriDayo/NekoBoxForAndroid/releases)
[![License: GPL-3.0](https://img.shields.io/badge/license-GPL--3.0-orange.svg)](https://www.gnu.org/licenses/gpl-3.0)

sing-box / universal proxy toolchain for Android.

一款使用 sing-box 的 Android 通用代理软件.

## 下载 / Downloads

[![GitHub All Releases](https://img.shields.io/github/downloads/Matsuridayo/NekoBoxForAndroid/total?label=downloads-total&logo=github&style=flat-square)](https://github.com/Matsuridayo/NekoBoxForAndroid/releases)

[GitHub Releases 下载](https://github.com/Matsuridayo/NekoBoxForAndroid/releases)

**Google Play 版本自 2024 年 5 月起已被第三方控制，为非开源版本，请不要下载。**

**The Google Play version has been controlled by a third party since May 2024 and is a non-open
source version. Please do not download it.**

## 更新日志 & Telegram 发布频道 / Changelog & Telegram Channel

https://t.me/Matsuridayo

## 项目主页 & 文档 / Homepage & Documents

https://matsuridayo.github.io

## 支持的代理协议 / Supported Proxy Protocols

* SOCKS (4/4a/5)
* HTTP(S)
* SSH
* Shadowsocks
* VMess
* Trojan
* VLESS
* AnyTLS
* ShadowTLS
* TUIC
* Hysteria 1/2
* WireGuard
* Trojan-Go (trojan-go-plugin)
* NaïveProxy (naive-plugin)
* Mieru (mieru-plugin)

请到[这里](https://matsuridayo.github.io/nb4a-plugin/)下载插件以获得完整的代理支持.

Please visit [here](https://matsuridayo.github.io/nb4a-plugin/) to download plugins for full proxy
supports.

## 支持的订阅格式 / Supported Subscription Format

* 一些广泛使用的格式 (如 Shadowsocks, ClashMeta 和 v2rayN)
* sing-box 出站

仅支持解析出站，即节点。分流规则等信息会被忽略。

* Some widely used formats (like Shadowsocks, ClashMeta and v2rayN)
* sing-box outbound

Only resolving outbound, i.e. nodes, is supported. Information such as diversion rules are ignored.

## 捐助 / Donate

<details>

如果这个项目对您有帮助, 可以通过捐赠的方式帮助我们维持这个项目.

捐赠满等额 50 USD 可以在「[捐赠榜](https://mtrdnt.pages.dev/donation_list)」显示头像, 如果您未被添加到这里,
欢迎联系我们补充.

Donations of 50 USD or more can display your avatar on
the [Donation List](https://mtrdnt.pages.dev/donation_list). If you are not added here, please
contact us to add it.

USDT TRC20

`TRhnA7SXE5Sap5gSG3ijxRmdYFiD4KRhPs`

XMR

`49bwESYQjoRL3xmvTcjZKHEKaiGywjLYVQJMUv79bXonGiyDCs8AzE3KiGW2ytTybBCpWJUvov8SjZZEGg66a4e59GXa6k5`

</details>

## 编译与签名构建 / Build & Signing

### 1. 准备工作

* 确保已安装 Android SDK、NDK 以及 Go 环境（如需编译 libcore）。
* 下载规则资源文件（GeoIP / GeoSite）：
  ```bash
  bash buildScript/lib/assets.sh
  ```
* 编译或准备 `libcore.aar`（如 `app/libs/libcore.aar` 不存在）：
  ```bash
  ./run lib core
  ```

### 2. 生成自签名密钥 (Self-sign Keystore)

项目默认会在项目根目录下查找名为 `release.keystore` 的密钥文件。如果没有，可以使用 `keytool` 生成一个：

```bash
keytool -genkeypair -v \
  -keystore release.keystore \
  -alias my-key-alias \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000 \
  -storepass your_password \
  -keypass your_password
```

### 3. 配置签名参数

在项目根目录的 `local.properties` 中添加签名信息（或者配置对应的环境变量）：

```properties
KEYSTORE_PASS=your_password
ALIAS_NAME=my-key-alias
ALIAS_PASS=your_password
```

> **注意**：如果之前运行过 Gradle 守护进程，修改 `local.properties` 后建议执行 `./gradlew --stop` 停止 daemon，以确保配置生效。

可以通过以下命令检查签名配置是否正确识别：
```bash
./gradlew app:signingReport
```
查看 `Variant: ossRelease` 是否已成功关联到密钥且无报错。

### 4. 命令行编译 Release APK

执行以下命令构建 Release 版本（会自动按 ABI 进行切分，包含 arm64-v8a、armeabi-v7a 等）：

```bash
./gradlew app:assembleOssRelease
```

编译完成后，APK 文件输出在：
```bash
app/build/outputs/apk/oss/release/
```
若配置了签名，arm64 架构的文件名类似于 `NekoBox-<version>-arm64-v8a.apk`（未签名则会带有 `-unsigned.apk` 后缀）。

### 5. 校验 APK 签名

使用 Android SDK 的 `apksigner` 工具检查生成的 APK 签名：

```bash
$ANDROID_HOME/build-tools/<version>/apksigner verify --verbose app/build/outputs/apk/oss/release/*arm64-v8a*.apk
```
若签名成功，输出中会显示 `Verifies` 以及签名 Schemes（如 v1/v2/v3 signing: true）。

## Credits

Core:

- [SagerNet/sing-box](https://github.com/SagerNet/sing-box)

Android GUI:

- [shadowsocks/shadowsocks-android](https://github.com/shadowsocks/shadowsocks-android)
- [SagerNet/SagerNet](https://github.com/SagerNet/SagerNet)

Web Dashboard:

- [Yacd-meta](https://github.com/MetaCubeX/Yacd-meta)
