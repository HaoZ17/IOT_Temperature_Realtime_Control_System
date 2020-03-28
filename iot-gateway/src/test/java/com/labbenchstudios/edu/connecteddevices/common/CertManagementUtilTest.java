/**
 * Copyright (c) 2018-2019. Andrew D. King. All Rights Reserved.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.labbenchstudios.edu.connecteddevices.common;

import javax.net.ssl.SSLSocketFactory;

import org.junit.Test;

/**
 * Simple test class for
 * {@link com.labbenchstudios.edu.connecteddevices.common.CertManagementUtil}.
 * 
 * @author aking
 */
public class CertManagementUtilTest
{
	// private var's
	
	/**
	 * NOTE: Copy a valid cert into the given file name, and uncomment
	 * the @Test annotation before {@link #testImportOfCertFromValidFile}
	 * to test.
	 */
	private String _testValidFileName    =
		"E:\\aking\\Documents\\workspace\\connected-devices-java\\src\\test\\java\\com\\labbenchstudios\\edu\\connecteddevices\\common\\test_cert_validA.pem";
	
	private String _testInvalidFileNameA =
		"E:\\aking\\Documents\\workspace\\connected-devices-java\\src\\test\\java\\com\\labbenchstudios\\edu\\connecteddevices\\common\\test_cert_emptyA.pem";
	
	private String _testInvalidFileNameB =
		"E:\\aking\\Documents\\workspace\\connected-devices-java\\src\\test\\java\\com\\labbenchstudios\\edu\\connecteddevices\\common\\test_cert_emptyB.pem";
	
	private String _testInvalidFileNameC =
		"E:\\aking\\Documents\\workspace\\connected-devices-java\\src\\test\\java\\com\\labbenchstudios\\edu\\connecteddevices\\common\\test_cert_emptyC.pem";
	
	private String _testNonExistentFileName =
		"E:\\aking\\Documents\\workspace\\connected-devices-java\\src\\test\\java\\com\\labbenchstudios\\edu\\connecteddevices\\common\\non_existent_file.pem";
	
	// test methods
	
	/**
	 * Tests {@link CertManagementUtil} with a certificate file
	 * containing one or more certificates.
	 * <p>
	 * NOTE: To include in JUnit tests, uncomment the @Test annotation.
	 * 
	 */
//	@Test
	public void testImportOfCertFromValidFile()
	{
		CertManagementUtil certMgr = CertManagementUtil.getInstance();
		SSLSocketFactory   factory = certMgr.loadCertificate(_testValidFileName);
		
		org.junit.Assert.assertNotNull(factory);
	}
	
	/**
	 * Tests {@link CertManagementUtil} with a null certificate file.
	 * 
	 */
	@Test
	public void testImportOfCertFromNullFile()
	{
		CertManagementUtil certMgr = CertManagementUtil.getInstance();
		SSLSocketFactory   factory = certMgr.loadCertificate(null);
		
		org.junit.Assert.assertNull(factory);
	}
	
	/**
	 * Tests {@link CertManagementUtil} with an invalid certificate file
	 * containing only the BEGIN / END entries.
	 * 
	 */
	@Test
	public void testImportOfCertFromEmptyFileA()
	{
		CertManagementUtil certMgr = CertManagementUtil.getInstance();
		SSLSocketFactory   factory = certMgr.loadCertificate(_testInvalidFileNameA);
		
		org.junit.Assert.assertNull(factory);
	}
	
	/**
	 * Tests {@link CertManagementUtil} with an invalid certificate file
	 * containing invalid text.
	 * 
	 */
	@Test
	public void testImportOfCertFromEmptyFileB()
	{
		CertManagementUtil certMgr = CertManagementUtil.getInstance();
		SSLSocketFactory   factory = certMgr.loadCertificate(_testInvalidFileNameB);
		
		org.junit.Assert.assertNull(factory);
	}
	
	/**
	 * Tests {@link CertManagementUtil} with an empty certificate file.
	 * 
	 */
	@Test
	public void testImportOfCertFromEmptyFileC()
	{
		CertManagementUtil certMgr = CertManagementUtil.getInstance();
		SSLSocketFactory   factory = certMgr.loadCertificate(_testInvalidFileNameC);
		
		org.junit.Assert.assertNull(factory);
	}
	
	/**
	 * Tests {@link CertManagementUtil} with a non-existent certificate file.
	 * 
	 */
	@Test
	public void testImportOfCertFromNonExistentFile()
	{
		CertManagementUtil certMgr = CertManagementUtil.getInstance();
		SSLSocketFactory   factory = certMgr.loadCertificate(_testNonExistentFileName);
		
		org.junit.Assert.assertNull(factory);
	}
}
