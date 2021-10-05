# Javaでのソケット通信（クライアント側）

ソケット通信のAPIを実装する必要があったので備忘録として残す。
## データの送受信はOSの仕事
Javaで関与できるのはバッファにデータを書き込む（読み込む）までのよう。<br>その先はOSによってTCP/IPプロトコルに従ってデータの送受信が行われる。
## 書き込み（読み込み）完了の監視
書き込みも読み込みも、必ずNバイト書き込む（読み込む）保証はないらしいので、要件によってはちゃんと書き込めた（読み込めた）を監視する必要がある。
<br>書き込み時の例
```
DataOutPutStream dos = new DataOutputStream(socket.getOutPutStream());
byte[] sendData;
int pos = dos.size();
while (pos < sendData.length) {
  dos.write(sendData, pos, sendData.length);
  pos = dos.size();
}
```
<br>読み込み時の例
```
DataInPutStream dis = new DataInputStream(socket.getInPutStream());
byte[] recvData;
boolean end = false;
int pos = 0;
while (!end) {
  pos += dis.read(recvData, pos, recvData.length);
  if (pos >= recvData.length) {
    end = true;
  }
}
```
## ブロッキング/ノンブロッキング
Socketクラスは処理をブロックする。ソケットを接続しに行くconnect()、データをストリームに書き込むwrite()*1、ストリームからデータを読み出すread()*2メソッドではそれぞれ接続、書き込み、読み込みが完了するまでは後続の処理をブロックする。このうち、書き込みに関しては特に問題にならないと思う。書き込み失敗時はIOExceptionが送出されるためwrite()で処理がブロックされるので、問題になるとすれば巨大なデータを書き込むときくらいだろう。もしもそんなデータの読み書きが必要ならばSocketクラスではなく、後述するSocketChannelクラスを使うとよいだろう。connect()とread()に関してはタイムアウト値の設定が可能であり、適切に設定することで処理をブロックする時間をコントロール可能だ。ただしreadのタイムアウト設定はreadを呼ぶよりも前に設定しておかないと効果がないことには注意が必要だ。
```
// connect()
Socket socket = new Socket();
socket.connect(new InetSocketAddress(ip, port), timeout);

// read()
InputStream is = socket.getInputStream();
socket.setSoTimeout(timeout);
byte[] recvData = new byte[1024];
is.read(recvData);
```
ノンブロッキングなソケット通信を利用したい場合はSocketChannelクラスを利用すべきだろう。こちらは今回は利用しなかったので詳細は割愛する。

*1：SocketではなくOutputtreamクラスのメソッドである。
*2：SocketではなくInputStreamクラスのメソッドである。
