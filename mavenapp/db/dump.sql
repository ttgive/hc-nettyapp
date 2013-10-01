CREATE TABLE IF NOT EXISTS `requests` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `src_ip` varchar(40) NOT NULL,
  `uri` varchar(256) NOT NULL,
  `when` datetime NOT NULL,
  `sent_bytes` int(11) NOT NULL DEFAULT '0',
  `received_bytes` int(11) NOT NULL DEFAULT '0',
  `redirect_url` varchar(256) NOT NULL,
  `timestamp` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1;