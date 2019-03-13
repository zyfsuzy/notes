------------------------
Zookeeper数据模型		|
------------------------
	# ZK的数据模型和标准的系统非常相似
		* 拥有层次命名空间,都是采用树形结构
	
	# ZK树中的每一个节点都被称为:Znode
		* 和文件系统树一样,ZK树中的每个节点可以拥有子节点
	
	# Znode具有文件和目录的特点
		* 像文件一样维护是着数据,元信息,ACL,时间戳等数据结构
		* 像目录一样可以作为路径标识的一部分,并可以具有子Znode
		* 用户在权限允许的情况下,可以对Znode进行CRUD操作
	
	# Znode具有原子性操作
		* 读操作将获取与节点相关的所有数据
		* 写操作也将替换掉节点的所有数据
		* 每个节点都拥有自己的ACL(访问控制列表),这列表规定了用户权限(指定用户指南进行指定的操作)
	
	# Znode 存在的数据大小有限制
		* ZK可以关联一些数据,但是它并不是数据库
		* 它用来管理调度数据,比如分布式应用中配置文件信息,状态,位置等等...这些数据都是很小的数据,通常都是KB为单位
		* 服务器和客户端都被设计为严格检查并限制每个Znode的数据大约是1MB(应该尽量小余此值)
	
	# Znode 通过路径引用
		* 跟Unix中的文件路径一样,路径必须是绝对的,因此必须以斜杠开头: /
		* 并且路径是唯一的,每个路径只有一个表示,因此这些路径不能修改
		* ZK中有一些限制字符串,例如:/zookeeper 用以保存管理信息,比如关键配额信息

	
	# 每个Znode由3部分组成
		stat
			* 状态信息,描述该Znode的版本,权限等信息
		data
			* 与该Znode关联的数据
		children
			* 该Znode下的子节点
		
	# 节点类型
		* 临时节点
			- 会话级别的节点,一点会话结束,临时节点将会被删除(会有几秒的延迟,因为系统要时间去判断客户端是真的断开了连接,还是网络异常)
			- 可以手动删除临时节点
			- 临时节点不允许有子节点

		* 永久节点
			- 该节点的只有在客户端主动执行删除的时候才会被删除
			
		* 节点的类型在创建的时候就已经确定,并且不能被改变
	
	# 序列化特性
		* 在在创建Znode的时候开启该特性的的话,Znode的名字后面会追加一个不断增加的序列号
		* 序列号对于此父节点来说是唯一的,这样便能记录每个子节点的先后创建顺序
		* 格式为: "%10d",前面空的补0: 0000000001
		
	
	# 根据上面两个特性,于是存在了4种节点
		PERSISTENT
			* 永久节点
		PERSISTENT_SEQUENTIAL
			* 永久节点序列化
		EPHEMERAL
			* 临时节点
		EPHEMERAL_SEQUENTIAL
			* 临时节点序列化
	
------------------------
Zookeeper节点属性		|
------------------------
	# 每个Znode都包含了一系列的属性,可以通过命令get来查看


	dataVersion
		* 数据版本号,每次对节点进行set操作,该值都会+1
		* 乐观锁

	cversion
		* 子节点版本号,默认是0
		* 当自节点有变化的时候,该值+1
		* 仅仅是添加,删除子节点的时候会影响该值,任意子节点本身都数据变化都不会影响它

	aclVersion
		* CAL版本号

	cZxid
		* Znode创建时的事务id

	mZxid
		* Znode被修改时的事务id,也就是每次对Znode的修改都会更新mZxid
		* 对于ZK来说,每次的变化(操作)都会产生一个唯一的事务id - zxid(Zookeeper Transaction Id)
		* 通过zxid可以确定更新操作的先后顺序
			- 例如 zxid1 < zxid2 说明zxid1的操先于zxid2发生
		* zxid在集群中全局唯一
	
	pZxid
		* 子节点的最后一次事务id
		* 包含子节点的创建,删除,修改

	ctime
		* 节点创建的时间戳

	mtime
		* 节点最后一次数据更新发生的时间戳
	
	ephemeralOwner
		* 如果节点为临时节点,那么该值表示与其绑定的sessionId
		* 如果不是临时的,那么该值为0x0

	dataLenth
		*  数据的长度

	numChildren
		* 子节点数量
