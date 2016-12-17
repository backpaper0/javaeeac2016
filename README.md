# Jersey OAuth 2 クライアントのサンプル

[Java EE Advent Calendar 2016](http://qiita.com/advent-calendar/2016/javaee)の17日目です。

* http://qiita.com/backpaper0@github/items/1cab8474f4764c6e1098

## 概要

[Jerseyのoauth2-client](http://repo.maven.apache.org/maven2/org/glassfish/jersey/security/oauth2-client/)を使ったサンプルです。

http://localhost:8080/helloworld/ へアクセスするとAuthorization Serverへリダイレクトしますので、ログインしてください。
ログインできるユーザー:パスワードは次の通りです。

|メールアドレス|パスワード|
|---|---|
|`user`|`password`    |
|`admin`|`password`    |

ログインしたらクライアントにリソースへのアクセスを許可するかどうかの選択を行ってください。

アクセスを許可すると、元のページへリダイレクトして`hello, <ユーザ名>!`が表示されます。
ユーザ名は認証したユーザの名前です。
これはアクセストークンを利用して認可済みのリソースとして取得しています。

Authorization ServerはSpring BootとSpring Security OAuthを利用して作成しました。
（参考：[From Zero to Hero with REST and OAuth2](https://github.com/Pivotal-Japan/from-zero-to-hero-with-rest-and-oauth2)）

## ビルド方法

helloworldアプリケーションとAuthorization Server（兼 Resource Server）をそれぞれビルドしてください。

### helloworldアプリケーションのビルド

```console
cd helloworld
gradlew uberJar
```

### Authorization Serverのビルド

```console
cd authorization
gradlew build
```

## 起動方法

helloworldアプリケーションとAuthorization Server（兼 Resource Server）をそれぞれ起動してください。

### helloworldアプリケーションの起動

```console
cd helloworld
java -jar build/libs/helloworld.jar --noCluster
```

次のようなログが出力されたら起動完了です。

> [2016-12-17T15:20:31.471+0900] [Payara Micro 4.1] [INFO] [] [PayaraMicro] [tid: _ThreadID=1 _ThreadName=main] [timeMillis: 1481955631471] [levelValue: 800] Deployed 1 archives

### Authorization Serverの起動

```console
cd authorization
java -jar build/libs/authorization.jar
```

次のようなログが出力されたら起動完了です。

> 2016-12-17 15:18:41.377  INFO 45968 --- [           main] com.example.AuthorizationApplication     : Started AuthorizationApplication in 7.225 seconds (JVM running for 7.817)

## helloworld-spring🍀

Springで作成したhelloworldアプリケーションも用意しました。

## License

Licensed under [The MIT License](https://opensource.org/licenses/MIT)
