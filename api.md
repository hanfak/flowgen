Builder builder = new Builder() 
    - create builder
builder.withConfig(config)
    - provide config (another builder)
builder.withGroup(builder)
builder.withAction(action)
    - provide action builder 
builder.withIf(from, ifbuilder)
builder.withIf(ifbuilder)
builder.withSplit(from, builder)
builder.withSplit(builder)
builder.withLoop(builder)
builder.render()
    - combine all state
    - create string
builder.renderToFile(location)
    - create file of string, calls render()

configbuilder

ifbuilder