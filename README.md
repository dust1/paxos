# 一个简单的paxos算法实现

# 什么是Paxos？
来源：[Paxos Made Simple](https://www.microsoft.com/en-us/research/uploads/prod/2016/12/paxos-simple-Copy.pdf)


一致性算法需要在一组服务器节点或者进程中保证一个提案能够在最终结果上达成一致。
这里的提案可以是对一次操作的认定，也可以衍生为对一个值的最终结果。

paxos算法中，由三个角色组成：Proposers(提议者)、Acceptors(接受者)、Learners(学习者)
在一个实现中，单个进程可以充当多个角色。

从安全角度来说：
* 只有从提案发起者发起的提案才能被选定是否通过
* 在最终结果没有出来前，任何节点/进程不能假设某个提案已经通过或者某个提案会有很大概率通过

这就导致了如下提案生成算法：
1. Proposer选择一个新的提案编号n，然后向某个Acceptor集合中的成员发送请求，要求他做出如下回应：
   * 保证不再通过任何编号小于n的提案
   * 返回它当前已经通过的编号小于n的最大编号提案，如果存在的话

我们把这样的请求称为编号n的prepare请求。

2. 如果Proposer收到来自集合中多数成员的响应结果，那么它可以提出编号为n，value值为v的提案，这里v时所有响应中最大编号提案的value值，如果响应中不包含任何提案，那么这个值就由Proposer自由决定。

把Proposer和Acceptor的行为结合起来，我们就能得到算法的两阶段执行过程：

Phase 1:
* Proposer选择一个提案编号n，然后向Acceptor的多数集发送编号为n的prepare请求。
* 如果一个Acceptor收到一个编号为n的prepare请示，且n大于它所有已经响应的请求的编号，那么他就会保证不会再通过任意编号小于n的提案，同时将它已经通过的最大编号提案（如果存在的话）一并作为响应。

Phase 2:
* 如果Proposer收到多数Acceptor对他的prepare请求（编号为n）的响应，那么它就会发送一个编号为n，value值为v的提案的acceptor请求给每个Acceptor，这里v是收到的响应中最大编号提案的值，如果响应中不包含任何提案，那么他就可以是任意值。
* 如果acceptor收到一个编号为n的提案的accept请求，只要它还未对编号大于n的prepare作出响应，他就可以通过这个提案

会有这种情况，两个Proposer轮流发起prepaer请求，但是永远没有一个Proposer进入plase2阶段。
为了保证进度，必须选择一个特定的Proposer作为唯一的提案提出者。如果这个Proposer可以和多数Acceptor进行通信，并且可以使用比已用编号更大的编号进行提案的话，那么它提出的提案就可以成功被通过。如果知道有某些编号更高的请求，他可以通过舍弃当前的提案并重新开始，这个Proposer最终一定会选到一个足够大的提案编号。这个Proposer叫做Leader。

Paxos算法假设了一组进程网络。在他的一致性算法中，每个进程都扮演着Proposer，Acceptor，以及Learner的角色。
该算法选择了一个Leader来扮演那个特定的Proposer和Learner。Paxos一致性算法就是上面描述的那样，请求和响应都以普通消息的方式发送（响应消息通过对应的提案编号来标识以免混淆）。使用可靠的存储设备存储Acceptor需要记住的信息来防止出错。
