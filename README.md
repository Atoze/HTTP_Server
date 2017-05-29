# HTTP_Server　[![CircleCI](https://circleci.com/gh/Atoze/HTTP_Server.svg?style=svg)](https://circleci.com/gh/Atoze/HTTP_Server)

## Description
簡易的なHTTPサーバ.

## Requirement
Java 1.8.0

## Usage
起動方法　　
* 1.cloneする　　
* 2.IntelliJにて開く.　
* 3.`./gradlew run`を、ルートフォルダで行う.　
* 終了の際は、強制終了させてください.

## Specification
ポート番号は Main.java の `PORT` で指定できます.<br>
デフォルトポート番号は "8080" です.

ルートディレクトリは、Server.java の `ROOT_DIRECTORY` より変更できます..<br>
デフォルトルートディレクトリは、`/src/main/resources` です.

HTTPエラーの際は、ルートディレクトリに入っている "HTTPステータスコード" + .html を参照します.<br>
ファイルが存在しない場合は、簡易的なテンプレートに沿ったHTMLを返します.

HTTPResponse#setResponseBodyにて、文字列とFile型にそれぞれ代入できますが、
両方代入した場合はFile型の方が優先されます.

`/program/board/`にアクセスすると、簡易的な掲示板が表示されます。

SSLは非対応です.

## Author
[Atoze](https://github.com/Atoze)
